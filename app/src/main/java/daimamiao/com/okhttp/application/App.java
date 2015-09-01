package daimamiao.com.okhttp.application;

import android.app.Application;

/**
 * Created by pengying on 2015/8/26.
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }

    public static App getAppContext(){
        return instance;
    }

    public static boolean isDebug() {
        return true;
    }

    public static boolean isDebugRequest() {
        return false;
    }
}
