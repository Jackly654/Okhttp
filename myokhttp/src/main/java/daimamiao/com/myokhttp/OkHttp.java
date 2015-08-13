package daimamiao.com.myokhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

    public void call(){
        Request.Builder builder = new Request.Builder();
        //设置取消的tag
        builder.tag();
        //判断get或者post
        if(boolean){
        builder.url();
        }else{
            builder.url()
        }
       mClient.newCall(builder.build()).enqueue(getRequestParamsCallback());
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
}
