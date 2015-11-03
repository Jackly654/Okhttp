package daimamiao.com.myokhttp;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import daimamiao.com.myokhttp.preference.config.NetConfig;
import daimamiao.com.myokhttp.preference.preference.ConfigName;
import daimamiao.com.myokhttp.preference.preference.PreferenceUtils;
import daimamiao.com.myokhttp.utils.DeviceUtils;
import daimamiao.com.myokhttp.utils.EncryptUtils;
import daimamiao.com.myokhttp.utils.JsonUtls;
import daimamiao.com.myokhttp.utils.Loger;
import daimamiao.com.myokhttp.utils.PackageUtil;
import daimamiao.com.myokhttp.utils.RunnableUtils;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/12.
 */
public class OkHttp implements HttpInterface{

    private Context mContext;
    private NetConfig mConfig;
    private static final OkHttpClient mClient;
    private static final Handler mHandler;
    private static final MediaType MEDIA_TYPE = MediaType.parse("image/*");

    static {
        mClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        mClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mClient.setReadTimeout(30, TimeUnit.SECONDS);
    }

    public OkHttp(){
    }

    public OkHttp(Context context,NetConfig config){
        this.mContext = context;
        this.mConfig = config;
    }

    //post处理文件
    public void call(NetConfig config,HttpManager.ResponseListener listener,boolean isExecute,Object... params){
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
            builder.post(getRequestBody(config,params,null).build());
        }
       mClient.newCall(builder.build()).enqueue(new Callback() {
           @Override
           public void onFailure(Request request, IOException e) {
                if(listener!=null){
                    runAction(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(false,e);
                        }
                    },false);
                }
           }

           @Override
           public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    String body = null;
                    body = response.body().string();
                    //TODO 数据库插入保存 （需要自己添加
                    Map<String, String> params = JsonUtls.getResonseDataMap(body);
                    if(params!=null&&listener!=null){
                        //后台确定
                        Boolean isSuccess = Boolean.valueOf(params.get("success"));
                        int code = JsonUtls.getRequestNumber(params.get("error_code"));
                        runAction(new Runnable() {
                            @Override
                            public void run() {
                                listener.onSuccess(isSuccess, code, params.get("items"));
                            }
                        }, isExecute);
                    }else if(listener!=null){
                        runAction(new Runnable() {
                            @Override
                            public void run() {
                               listener.onFail(false,null);
                            }
                        },false);
                    }
                }else if(listener!=null){
                    String body = response.body().string();
                    //Loger.appendInfo(OkHttp.this, response.message() + "-----" + body);
                    runAction(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(false, new NullPointerException());
                        }
                    }, false);
                }
               removeCall(action);
           }
       });
    }

    private MultipartBuilder getRequestBody(NetConfig config,Object[] paramsValues,File[] files) {
        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        ArrayList<Pair<String, String>> paramPair = new ArrayList<>();
        if (config != null) {
            String[] paramsNames = config.params;
            if (paramsNames != null && paramsValues != null) {
                int length = paramsNames.length;
                for (int i = 0; i < length; i++) {
                    if (paramsNames[i] != null && i < paramsValues.length) {
                        if (config.filter) {
                            if (paramsValues[i] != null && !"-1".equals(paramsValues[i].toString())) {
                                builder.addFormDataPart(paramsNames[i], paramsValues[i].toString());
                                paramPair.add(new Pair(paramsNames[i], paramsValues[i].toString()));

                            }
                        } else {
                            builder.addFormDataPart(paramsNames[i], paramsValues[i].toString());


                            paramPair.add(new Pair(paramsNames[i], paramsValues[i].toString()));
                        }
                    }
                }
            }
            if (files != null && files.length != 0) {
                //添加文件上传
                String[] paramsFiles = config.files;
                if (paramsFiles != null && files != null) {
                    int length = paramsFiles.length;
                    for (int i = 0; i < length; i++) {
                        if (paramsFiles[i] != null && i < paramsFiles.length) {
                            builder.addFormDataPart(paramsFiles[i], files[i].getName(), RequestBody.create(MEDIA_TYPE, files[i]));
                        }
                    }
                }
            }
        }
        if (config.addExtras) {
            builder = addPostExtrasParams(builder, paramPair, config.version);
        }
        return builder;
    }

    private MultipartBuilder addPostExtrasParams(MultipartBuilder builder, ArrayList<Pair<String, String>> pairs, int version) {
        ArrayList<Pair<String, String>> requestPair = getRequestPair(version);
        pairs.addAll(requestPair);
        int size = requestPair.size();
        for (int i = 0; i < size; i++) {
            Pair<String, String> pair = requestPair.get(i);
            if(!TextUtils.isEmpty(pair.second))
                builder.addFormDataPart(pair.first, pair.second);
        }
        builder.addFormDataPart("sign", getSignValue(pairs));
        return builder;
    }

    public static ArrayList<Pair<String, String>> getRequestPair(int version) {
        ArrayList<Pair<String, String>> pairs = new ArrayList<>();
        Context appContext = App.getAppContext();
        String channel = PackageUtil.getStringMataData("UMENG_CHANNEL");
        String appVersion = PackageUtil.getAppVersoin();
        String imei= DeviceUtils.getIMEI();
        switch (version) {
            case 3:
//              iid ,device_id  ,access,channel,app_version,version_code,device_platform,os_version,os_api,device_model ,uuid(imei) ,openudid(device id)
                String iid = PreferenceUtils.getString(ConfigName.IID);
                String deviceId = PreferenceUtils.getString(ConfigName.DEVICE_NEW_ID);
                if (!TextUtils.isEmpty(iid)) {
                    pairs.add(new Pair("iid", iid));
                }
                if (!TextUtils.isEmpty(deviceId)) {
                    pairs.add(new Pair("device_id", deviceId));
                }
                pairs.add(new Pair("access", HttpManager.getNetWorkState(appContext)));//网络状态 3G/4G...
                pairs.add(new Pair("channel", channel));//渠道
                pairs.add(new Pair("app_version", appVersion));//应用版本号
                pairs.add(new Pair("version_code", String.valueOf(PackageUtil.getAppCode())));//应用版本号
                pairs.add(new Pair("device_platform", "android"));//设置系统名称
                pairs.add(new Pair("os_version", String.valueOf(android.os.Build.DISPLAY)));//系统版本号
                pairs.add(new Pair("os_api", String.valueOf(Build.VERSION.SDK_INT)));//系统初始化id
                pairs.add(new Pair("device_model", String.valueOf(Build.MODEL)));
                if(!TextUtils.isEmpty(imei)) {
                    pairs.add(new Pair("uuid", imei));//imei
                }
                String androidId=DeviceUtils.getAndroidId();
                if(!TextUtils.isEmpty(androidId)){
                    pairs.add(new Pair("openudid", androidId));
                }

                break;
            default:
                String did=PreferenceUtils.getDid();
                if(!TextUtils.isEmpty(did)) {
                    pairs.add(new Pair("phone_code", did));
                }
                pairs.add(new Pair("device_type", "2"));
                pairs.add(new Pair("phone_network", HttpManager.getNetWorkState(appContext)));
                pairs.add(new Pair("channel_code", channel));
                pairs.add(new Pair("client_version", appVersion));

                //防作弊字段
                if(!TextUtils.isEmpty(imei)) {
                    pairs.add(new Pair("device_id", imei));
                }
                String uuid=PreferenceUtils.getString(ConfigName.UUID);
                if(!TextUtils.isEmpty(uuid)) {
                    pairs.add(new Pair("uuid", uuid));
                }
                break;
        }
        //初始化用户uid
        String uid = PreferenceUtils.getString(ConfigName.USER_ID);
        if (!TextUtils.isEmpty(uid)) {
            pairs.add(new Pair("uid", uid));
        }
        //sim卡状态
        pairs.add(new Pair("phone_sim", DeviceUtils.hasSIMCard() ? "1" : "2"));
        //运营商
        String operatorName = DeviceUtils.getNetworkOperatorName();
        if (!TextUtils.isEmpty(operatorName)) {
            pairs.add(new Pair("carrier", DeviceUtils.getNetworkOperatorName()));
        }
        if (App.isDebugRequest()) {
            pairs.add(new Pair("debug", "1"));
        }
        return pairs;
    }

    public static String getSignValue(ArrayList<Pair<String, String>> params) {
        String signValue = null;
        if (null != params && !params.isEmpty()) {
            signValue = new String();
            Collections.sort(params, (Pair<String, String> lhs, Pair<String, String> rhs) -> {
                return lhs.first.compareTo(rhs.first);
            });
            int size = params.size();
            for (int i = 0; i < size; i++) {
                Pair<String, String> pair = params.get(i);
                signValue += (pair.first + "=" + pair.second);
            }
        }
        if (TextUtils.isEmpty(signValue)) {
            signValue = EncryptUtils.getMD5(NetWorkConfig.KEY);
        }
        return EncryptUtils.getMD5((signValue += NetWorkConfig.KEY));
    }

    public static String getRequestUrl(String url) {
        return getRequestUrl(url,/*ConfigName.SERVER_URL,*/NetWorkConfig.source());
    }

    public static String getRequestUrl(String url,String default_server_url) {
        String paramsUrl;
        paramsUrl = default_server_url + url;
        return paramsUrl;
    }



    private Callback getRequestParamsCallback(final String action,final HttpManager.ResponseParamsListener listener,final boolean isExecute,final Object[] params) {
        return new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if(listener!=null){
                    runAction(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(true,e);
                        }
                    },false);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        };
    }

    public void call(NetConfig config,HttpManager.ResponseParamsListener listener,boolean isExecute,Object[] params,File[] files){
        Request.Builder builder = new Request.Builder();
        String action = config.action;
        builder.tag(config);
        if("get".equals(config.method)){
            builder.url(getRequestParamsUrl());
        }else{
            String paramsUrl = getRequestUrl(config.url);
            builder.url(paramsUrl);
            builder.post(getRequestBody(config,params,files).build());

        }
        mClient.newCall(builder.build()).enqueue(getRequestParamsCallback(action,listener,isExecute,params));
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

    /**
     * 移除运行任务
     *
     * @param action
     */
    @Override
    public void removeCall(String action) {
        mClient.cancel(action);
    }

    @Override
    public void call(NetConfig config, HttpManager.ResponseParamsListener listener, Object[] params, File[] files) {
        call(config,listener,config.execute,params,files);
    }

    @Override
    public void call(NetConfig config, HttpManager.ResponseListener listener, Object... params) {
        call(config,listener,config.execute,params);
    }

    @Override
    public void request(String url, int method, ArrayList<Pair<String, String>> params, HttpManager.ResponseParamsListener listener) {

    }
}
