package daimamiao.com.myokhttp.utils;
import android.text.TextUtils;

import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/31.
 */
public class Loger {

    private static final String APP_TAG = "AppLog";
    private static final int LOG_LEVEL; // 当前日志显示等级

    private static final int NONE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;
    private static final int ALL = 5;

    private static final Loger logger;

    static {
        logger = new Loger(null);
        LOG_LEVEL = App.isDebug() ? ALL : NONE;
    }

    private static Loger getLogger(Object object) {
        logger.setPrefix((null != object) ? object.getClass().getSimpleName() + ": " : PackageUtil.getAppPackage() + ": ");
        return logger;
    }

    private String logPrefix;

    private Loger(String prefix) {
        logPrefix = prefix;
    }

    private void setPrefix(String prefix) {
        logPrefix = prefix;
    }

    private String getMsg(String msg) {
        if (!TextUtils.isEmpty(logPrefix)) {
            return logPrefix + msg;
        } else {
            return "Other:" + msg;
        }
    }

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(Object object, String msg) {
        if (INFO < LOG_LEVEL) {
            android.util.Log.i(APP_TAG, getLogger(object).getMsg(msg));
        }
    }

    public static void i(String msg, Throwable t) {
        i(null, msg, t);
    }

    public static void i(Object object, String msg, Throwable t) {
        if (INFO < LOG_LEVEL) {
            android.util.Log.i(APP_TAG, getLogger(object).getMsg(msg), t);
        }
    }

    public static void d(String msg) {
        d(null, msg);
    }

    public static void d(Object object, String msg) {
        if (DEBUG < LOG_LEVEL) {
            android.util.Log.d(APP_TAG, getLogger(object).getMsg(msg));
        }
    }

    public static void d(String msg, Throwable t) {
        d(null, msg, t);
    }

    public static void d(Object object, String msg, Throwable t) {
        if (DEBUG < LOG_LEVEL) {
            android.util.Log.d(APP_TAG, getLogger(object).getMsg(msg), t);
        }
    }

    public static void w(String msg) {
        d(null, msg);
    }

    public static void w(Object object, String msg) {
        if (WARN < LOG_LEVEL) {
            android.util.Log.w(APP_TAG, getLogger(object).getMsg(msg));
        }
    }

    public static void w(String msg, Throwable t) {
        w(null, msg, t);
    }

    public static void w(Object object, String msg, Throwable t) {
        if (WARN < LOG_LEVEL) {
            android.util.Log.w(APP_TAG, getLogger(object).getMsg(msg), t);
        }
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void e(Object object, String msg) {
        if (ERROR < LOG_LEVEL) {
            android.util.Log.e(APP_TAG, getLogger(object).getMsg(msg));
        }
    }

    public static void e(String msg, Throwable t) {
        e(null, msg, t);
    }

    public static void e(Object object, String msg, Throwable t) {
        if (ERROR < LOG_LEVEL) {
            android.util.Log.e(APP_TAG, getLogger(object).getMsg(msg), t);
        }
    }

   /* public static void appendInfo(String info) {
        appendInfo(null, info);
    }

    *//**
     * 记录操作信息
     *
     * @param info
     *//*
    public static void appendInfo(final Object object, final String info) {
        if (ERROR < LOG_LEVEL) {
            if (null == object) {
                String tag = "other";
                RecordManager.get().insertEvent(App.getAppContext(), EventType.LOG, info, tag);
            } else {
                //避免传入object进子线程回调内.以造成内存泄漏
                final String finalTag = object.getClass().getSimpleName();
                ConfigManager.get().runUiAction(object, new Task<CollectUIConfig>() {
                    @Override
                    public void run(CollectUIConfig config) {
                        RecordManager.get().insertEvent(App.getAppContext(), EventType.LOG, info, null != config ? config.name : finalTag);
                    }
                });
            }
        }
    }

    public static void appendError(String info) {
        appendError(null, info);
    }

    *//**
     * 记录操作信息
     *
     * @param info
     *//*
    public static void appendError(Object object,final String info) {
        if (ERROR < LOG_LEVEL) {
            if (null == object) {
                String tag = "other";
                RecordManager.get().insertEvent(App.getAppContext(), EventType.ERROR, info, tag);
            } else {
                //避免传入object进子线程回调内.以造成内存泄漏
                final String finalTag = object.getClass().getSimpleName();
                ConfigManager.get().runUiAction(object, new Task<CollectUIConfig>() {
                    @Override
                    public void run(CollectUIConfig config) {
                        RecordManager.get().insertEvent(App.getAppContext(), EventType.ERROR, info, null != config ? config.name : finalTag);
                    }
                });
            }
        }
    }
*/
}
