package daimamiao.com.myokhttp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import daimamiao.com.myokhttp.preference.preference.ConfigName;
import daimamiao.com.myokhttp.preference.preference.PreferenceUtils;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/31.
 */
public class DeviceUtils {
    public static void initID(Context context) {
        String id = getAndroidId();
        if (TextUtils.isEmpty(id)) {
            id = getIMEI();
            id = !TextUtils.isEmpty(id) ? id : UUID.randomUUID().toString()
                    .toString().replaceAll("-", "");
        }
        PreferenceUtils.setString(ConfigName.DEVICE_ID, id);
    }


    //sim卡是否可读
    public static boolean isCanUseSim(Context context) {
        boolean result = false;
        if (null != context) {
            try {
                TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                result = TelephonyManager.SIM_STATE_READY == mgr.getSimState();
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 获得android设备id
     *
     * @return
     */
    public static String getAndroidId() {
        String id = null;
        Context appContext = App.getAppContext();
        if (TextUtils.isEmpty(id)) {
            id = Secure.getString(appContext.getContentResolver(),
                    Secure.ANDROID_ID);
        }
        return id;
    }

    /**
     * 获得imei码
     *
     * @return
     */
    public static String getIMEI() {
        String id = null;
        Context context = App.getAppContext();
        if (null != context && isHasPermission("android.permission.READ_PHONE_STATE")) {
            id = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return id;
    }

    /**
     * 判断某个权限是否授权
     * @param permissionName 权限名称，比如：android.permission.READ_PHONE_STATE
     * @return
     */

    public static boolean isHasPermission(String permissionName){
        Context context = App.getAppContext();

        try {
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission(permissionName, context.getPackageName()));

            return permission;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 是否安装sim卡
     *
     * @return
     */
    public static boolean hasSIMCard() {
        Context appContext = App.getAppContext();
        TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);//取得相关系统服务
        return TelephonyManager.SIM_STATE_READY == tm.getSimState();
//            case TelephonyManager.SIM_STATE_ABSENT:
//                sb.append("无卡");
//                break;
//            case TelephonyManager.SIM_STATE_UNKNOWN:
//                sb.append("未知状态");
//                break;
//            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
//                sb.append("需要NetworkPIN解锁");
//                break;
//            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
//                sb.append("需要PIN解锁");
//                break;
//            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
//                sb.append("需要PUK解锁");
//                break;
//            case :
//                sb.append("良好");
//                break;
//        }
//
//        if (tm.getSimSerialNumber() != null) {
//            sb.append("@" + tm.getSimSerialNumber().toString());
//        } else {
//            sb.append("@无法取得SIM卡号");
//        }
//
//        if (tm.getSimOperator().equals("")) {
//            sb.append("@无法取得供货商代码");
//        } else {
//            sb.append("@" + tm.getSimOperator().toString());
//        }
//
//        if (tm.getSimOperatorName().equals("")) {
//            sb.append("@无法取得供货商");
//        } else {
//            sb.append("@" + tm.getSimOperatorName().toString());
//        }
//
//        if (tm.getSimCountryIso().equals("")) {
//            sb.append("@无法取得国籍");
//        } else {
//            sb.append("@" + tm.getSimCountryIso().toString());
//        }
//        if (tm.getNetworkOperator().equals("")) {
//            sb.append("@无法取得网络运营商");
//        } else {
//            sb.append("@" + tm.getNetworkOperator());
//        }
//        if (tm.getNetworkOperatorName().equals("")) {
//            sb.append("@无法取得网络运营商名称");
//        } else {
//            sb.append("@" + tm.getNetworkOperatorName());
//        }
//        if (tm.getNetworkType() == 0) {
//            sb.append("@无法取得网络类型");
//        } else {
//            sb.append("@" + tm.getNetworkType());
//        }
//        return sb.toString();
    }

    /**
     * 获得网络运营商名称
     *
     * @return
     */
    public static String getNetworkOperatorName() {
        String networkOperatorName = null;
        try {
            Context appContext = App.getAppContext();
            TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            networkOperatorName = tm.getNetworkOperatorName();
        } catch (Exception e) {
            networkOperatorName = null;
        }
        return networkOperatorName;
    }

    /**
     * 获取手机mac地址<br/>
     */
    public static String getMacAddress() {
        // 获取mac地址：
        String macAddress = null;
        try {
            Context appContext = App.getAppContext();
            WifiManager manager = (WifiManager) appContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == manager ? null : manager
                    .getConnectionInfo());
            if (null != info && !TextUtils.isEmpty(info.getMacAddress())) {
                macAddress = info.getMacAddress();
            }
        } catch (Exception e) {
        }
        return macAddress;
    }

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id() {
        if (sID == null) {
            Context context = App.getAppContext();
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

}
