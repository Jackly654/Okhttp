package daimamiao.com.myokhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import daimamiao.com.myokhttp.preference.config.NetConfig;
import daimamiao.com.myokhttp.utils.RunnableUtils;

/**
 * Created by pengying on 2015/8/12.
 */
public class OkHttp implements HttpInterface{

    private Context mContext;
    private static final OkHttpClient mClient;
    private static final Handler mHandler;

    static {
        mClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        mClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mClient.setReadTimeout(30, TimeUnit.SECONDS);
    }

    public OkHttp(){
    }

    public OkHttp(Context context){
        this.mContext = context;
    }

    //post处理文件
    public void call(NetConfig config){
        Request.Builder builder = new Request.Builder();
        final String action = config.action;
        //设置取消的tag
        builder.tag(action);
        //判断get或者post
        if("get".equals(config.method)){
        builder.url(getRequestParamsUrl());
        }else{
            String requestUrl = getRequestUrl(config.url);
            builder.url(requestUrl);
            builder.post(getRequestBody().build());
        }
       mClient.newCall(builder.build()).enqueue(getRequestParamsCallback());
    }

    private MultipartBuilder getRequestBody() {
        return null;
    }

    public static String getRequestUrl(String url) {
        return getRequestUrl(url,/*ConfigName.SERVER_URL,*/NetWorkConfig.source());
    }

    public static String getRequestUrl(String url,String default_server_url) {
        String paramsUrl;
        paramsUrl = default_server_url + url;
        return paramsUrl;
    }



    private Callback getRequestParamsCallback() {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        };
    }

    public void call(){
        Request.Builder builder = new Request.Builder();
        builder.tag();
        if("get".equals(config.method)){
            builder.url(getRequestParamsUrl());
        }else{
            builder.url();
            builder.post()

        }
    }

    private String getRequestParamsUrl() {
        return null;
    }

    /**
     * 处理action ,根据标记决定是否在主线程还是当前线程执行
     *
     * @param action
     * @param isExecute 是否在当前线程中运行
     */
    private void runAction(Runnable action, boolean isExecute) {
        if (null != action) {
            //在子线程中运行
            if (isExecute) {
                if (Looper.getMainLooper() != Looper.myLooper()) {
                    action.run();
                } else {
                    RunnableUtils.runWithExecutor(action);
                }
            } else {
                //在ui线程运行
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    action.run();
                } else {
                    mHandler.post(action);
                }
            }
        }
    }
}
