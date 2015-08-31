package daimamiao.com.myokhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import daimamiao.com.myokhttp.listener.Task;
import daimamiao.com.myokhttp.preference.ConfigManager;
import daimamiao.com.myokhttp.preference.config.NetConfig;
import daimamiao.com.myokhttp.utils.Loger;
import daimamiao.com.myokhttp.utils.RunnableUtils;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/12.
 */
public class HttpManager {
    public static final  OkHttp mOkHttp;
    private static final HashMap<String, ArrayList<String>> mRequestTags;
    private static final HashMap<Class<?>,Constructor<?>> mConstructors;
    static {
        mOkHttp = new OkHttp();
        mRequestTags = new HashMap<>();
        mConstructors = new HashMap<>();

    }

    /**
     * 判断当前apn列表中哪个连接选中了
     */
    public static boolean checkNetWork() {
        final Context context = App.getAppContext();
        boolean wifi = isWIFI(context);
        boolean moble = isMoble(context);
        // 如果两个连接都未选中
        if (!wifi && !moble) {
            return false;
        }
        return true;
    }

    /**
     * 判断wifi是否处于连接状态
     *
     * @return boolean :返回wifi是否连接
     */
    public static boolean isWIFI(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean result = false;
        if (networkInfo != null) {
            result = networkInfo.isConnected();
        }
        return result;
    }

    /**
     * 判断是否APN列表中某个渠道处于连接状态
     *
     * @return 返回是否连接
     */
    public static boolean isMoble(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean result = false;
        if (networkInfo != null) {
            result = networkInfo.isConnected();
        }
        return result;
    }

    /**
     * request请求
     * @param action
     * @param listener
     */
    public static  <T extends HttpInterface> void request(){
        RunnableUtils.
    }

    public static <T extends  HttpInterface> void request(Object object,String action, final Class clazz){
        if(checkNetWork()) {
            if (!TextUtils.isEmpty(action)) {
                cacheTag(object, action);
                ConfigManager.get().runNetAction(action,new Task<NetConfig>() {

                    @Override
                    public void run(NetConfig netConfig) {
                        try {
                        Context appContext = App.getAppContext();
                        Constructor constructor = null;
                        //复用构造器.提高效率
                        if(mConstructors.containsKey(clazz)){
                            constructor = mConstructors.get(clazz);
                        }
                        if(constructor == null){
                            constructor = clazz.getConstructor(Context.class,NetConfig.class);
                            mConstructors.put(clazz,constructor);
                        }
                            T t = (T) constructor.newInstance(appContext,netConfig);
                            t.call
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    }
                });
                request();
            }
        }
    }


    public static class SimpleResponseParamsListener implements ResponseParamsListener {

        @Override
        public void onSuccess(boolean isScuuess, int code, Map<String, String> params, String result) {
            Loger.i("result:" + result);
        }

        @Override
        public void onFail(boolean noNetwok, Exception error) {
        }
    }




    /**
     * request请求回调
     *
     * @author Administrator
     */
    public interface ResponseListener extends FailListener {
        /* 请求成功 */
        void onSuccess(boolean isScuuess, int code, String data);
    }

    /**
     * request请求回调
     *
     * @author Administrator
     */
    public interface ResponseParamsListener extends FailListener {
        /* 请求成功 */
        void onSuccess(boolean isScuuess, int code, Map<String, String> params, String result);
    }

    public interface FailListener {
        /* 请求失败 */
        void onFail(boolean noNetwok, Exception error);
    }

    /**
     * 缓存请求标记
     *
     * @param object
     * @param action
     */
    private static void cacheTag(Object object, String action) {
        if (null != object) {
            String key = object.toString();
            ArrayList<String> tags = mRequestTags.get(key);
            if (null == tags || tags.isEmpty()) {
                tags = new ArrayList<>();
                mRequestTags.put(key, tags);
            }
            tags.add(action);
        }
    }
}
