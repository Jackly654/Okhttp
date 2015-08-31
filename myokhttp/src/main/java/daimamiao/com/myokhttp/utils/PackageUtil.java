package daimamiao.com.myokhttp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import daimamiao.com.myokhttp.R;
import daimamiao.com.okhttp.application.App;

public abstract class PackageUtil {
    private static Uri marketUri(String pkg) {
        return Uri.parse("market://details?id=" + pkg);
    }

    public static boolean installFromMarket(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, marketUri(getAppPackage()));
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

   /* *//**
     * 自定义应用市场安装对话框,实现监听功能
     *
     * @param context
     *//*
    public static void customInstallFromMarket(final Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, marketUri(getAppPackage()));
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final ArrayList<ResolveInfo> resolves = new ArrayList<ResolveInfo>();
        if (null != resolveInfos) {
            resolves.addAll(resolveInfos);
        }
        //当没有应用商店时提示
        if (resolveInfos.isEmpty()) {
            ToastUtils.toast(R.string.sorry_no_market);
            return;
        }
        builder.setTitle(R.string.choice_market);
        builder.setAdapter(new AppChooseAdapter(context, resolves), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                dialog.dismiss();
                RunnableUtils.runWiteTryCatch(() -> {
                    ResolveInfo info = resolves.get(which);
                    ActivityInfo activity = info.activityInfo;
                    ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(name);
                    i.setData(marketUri(getAppPackage()));
                    context.startActivity(i);
                });
                HttpManager.request(null, NetWorkConfig.REVIEW_APP, new HttpManager.ResponseParamsListener() {
                    @Override
                    public void onSuccess(boolean isScuuess, int code, Map<String, String> params, String result) {
                        if (isScuuess) {
                            int score = JsonUtils.getRequestNumber(params.get("score"));
                            if (0 < score) {
                                //更新用户资料
                                BusProvider.post(new InitUserDataEvent());
                                ToastUtils.showColdToast(App.getStr(R.string.review_success, score));
                            }
                        }
                    }

                    @Override
                    public void onFail(boolean noNetwok, Exception error) {
                    }
                });
            }
        });
        builder.create().show();
    }*/


    /**
     * 启用设置界面
     *
     * @param context
     */
    public static void startSetting(Context context) {
        if (null != context) {
            try {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

    public static boolean canBeStarted(Context context, Intent intent, boolean checkSignature) {
        final PackageManager manager = context.getApplicationContext().getPackageManager();
        final ResolveInfo info = manager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            return false;
        }
        final ActivityInfo activityInfo = info.activityInfo;
        if (activityInfo == null) {
            return false;
        }
        if (!checkSignature) {
            return true;
        }
        return PackageManager.SIGNATURE_MATCH == manager.checkSignatures(context.getPackageName(), activityInfo.packageName);
    }

    /**
     * 获得软件版本
     *
     * @return
     */
    public static String getAppVersoin() {
        String appVersion = null;
        try {
            Context context = App.getAppContext();
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * 获得软件code
     *
     * @return
     */
    public static int getAppCode() {
        int appCode = -1;
        try {
            Context context = App.getAppContext();
            appCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            appCode = -1;
        }
        return appCode;
    }


    /**
     * 获得应用包名
     *
     * @return appPackage
     */
    public static String getAppPackage() {
        return App.getAppContext().getPackageName();
    }

    /**
     * 获得应用名
     *
     * @return appPackage
     */
    public static String getAppName() {
        return App.getAppContext().getString(R.string.app_name);
    }

    /**
     * 判断指定包名的进程是否运行
     *
     * @param packageName 指定包名
     * @return 是否运行
     */
    public static boolean isRunning(String packageName) {
        ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(100);
        boolean result = false;
        for (RunningTaskInfo info : runningTasks) {
            if (null != info && !TextUtils.isEmpty(info.baseActivity.getPackageName()) && info.baseActivity.getPackageName().equals(packageName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 本应用是否在前台运行
     * <p>
     * 指定包名
     *
     * @return 是否运行
     */
    public static boolean isAppRunning() {
        ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
        boolean result = false;
        for (RunningTaskInfo info : runningTasks) {
            if (null != info && !TextUtils.isEmpty(info.baseActivity.getPackageName()) && info.baseActivity.getPackageName().equals(getAppPackage())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static ApplicationInfo getAppInfo() {
        ApplicationInfo appInfo = null;
        try {
            Context appContext = App.getAppContext();
            appInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo;
    }

    /**
     * 安装指定文件apk
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        if (null != context && file.exists()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("application/vnd.android.package-archive");
            intent.setData(Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 获得string mataData数据
     *
     * @param key
     * @return
     */
    public static String getStringMataData(String key) {
        ApplicationInfo appInfo = getAppInfo();
        String value = null;
        if (null != appInfo) {
            value = appInfo.metaData.getString(key);
        }
        return value;
    }

    /**
     * 获得boolean mataData数据
     *
     * @param key
     * @return
     */
    public static boolean getBooleanMataData(String key) {
        ApplicationInfo appInfo = getAppInfo();
        boolean value = false;
        if (null != appInfo) {
            value = appInfo.metaData.getBoolean(key);
        }
        return value;
    }

    /**
     * 根据包名启动其他应用
     *
     * @param packageName
     */
    public static void startAppByPackage(String packageName) {
        Context appContext = App.getAppContext();
        if (null != appContext) {
            PackageManager packageManager = appContext.getPackageManager();
            Intent intent = null;
            try {
                intent = packageManager.getLaunchIntentForPackage(packageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != intent) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            }
        }
    }

    /**
     * 检查某个应用是否安装
     *
     * @param packageName
     */
    public static boolean appIsInstall(String packageName) {
        boolean install = false;
        Context appContext = App.getAppContext();
        if (!TextUtils.isEmpty(packageName) && null != appContext) {
            try {
                install = (null != appContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES));
            } catch (NameNotFoundException e) {
                install = false;
            }
        }
        return install;
    }

    /**
     * 获得安装apkintent
     *
     * @param target
     * @return
     */
    public static Intent getInstallIntent(String target) {
        Intent intent = null;
        if (!TextUtils.isEmpty(target)) {
            File file = new File(target);
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("application/vnd.android.package-archive");
            intent.setData(Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        return intent;
    }

    /**
     * 是否为当前app进程
     *
     * @param context
     * @return
     */
    public static boolean isCurrentProcess(Context context) {
        boolean result = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runProcess = am.getRunningAppProcesses();
        if (null != runProcess && !runProcess.isEmpty()) {
            result = android.os.Process.myPid() == runProcess.get(0).pid;
        }
        return result;
    }

    /**
     * 获得指定位置应用包名
     *
     * @param index 获得任务栈指定位置的应用包名
     * @return
     */
    public static String getPackage(int index) {
        ActivityManager am = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = am.getRunningTasks(index + 1);
        String pkg = null;
        if (null != runningTasks && !runningTasks.isEmpty()) {
            RunningTaskInfo info = runningTasks.get(index);
            if (null != info) {
                ComponentName baseActivity = info.baseActivity;
                if (null != baseActivity) {
                    pkg = baseActivity.getPackageName();
                }
            }
        }
        return pkg;
    }
}
