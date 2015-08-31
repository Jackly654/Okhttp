package daimamiao.com.myokhttp.preference.preference;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import java.lang.reflect.Field;

import daimamiao.com.myokhttp.utils.DeviceUtils;
import daimamiao.com.myokhttp.utils.Loger;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/31.
 */
public class PreferenceUtils {

    // 配置项名称
    private static final String DEFAULT_PREFERENCE = "jy_config";
    private static final String DEFAULT_KEY = "config";
    private static final String DEFAULT_VALUE = "-1";
    private static String[] values;


    @SuppressLint("InlinedApi")
    public static void init(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_PRIVATE);
        String value = preferences.getString(DEFAULT_KEY, null);
        //String initConfig = MyDb.getConfig("init_config");
        if (TextUtils.isEmpty(value) /*&& TextUtils.isEmpty(initConfig)*/) {
            // 初始化配置
            initPreference(preferences);
        } else {
            // 确定是否新增配置项
//			ensureValue(false);
            values = value.split("\\|");
            Field[] fields = ConfigName.class.getFields();
            String[] preferenceValue = values;
            if (fields.length != preferenceValue.length) {
                // 添加配置项_
                if (fields.length > preferenceValue.length) {
                    String[] newValue = new String[fields.length];
                    System.arraycopy(preferenceValue, 0, newValue, 0, preferenceValue.length);
                    // 为剩余取值赋默认值
                    for (int i = preferenceValue.length; i < fields.length; i++) {
                        newValue[i] = String.valueOf(DEFAULT_VALUE);
                    }
                    values = newValue;
                    createValue(newValue);
                    //Loger.appendInfo("append_value:" + newValue);
                }
            }
        }
    }

    /**
     * 初始化配置项
     */
    private static void initPreference(SharedPreferences preferences) {
        Field[] fields = ConfigName.class.getFields();
        if (null != fields && 0 < fields.length) {
            int length = fields.length;
            String value = new String();
            for (int i = 0; i < length; i++) {
                if (length - 1 != i) {
                    value += DEFAULT_VALUE + "|";
                } else {
                    value += DEFAULT_VALUE;
                }
            }
            preferences.edit().putString(DEFAULT_KEY, value).commit();
        }
    }

    /**
     * 设置取值
     *
     * @param value
     */
    @SuppressLint("InlinedApi")
    private static void setValue(String value) {
        SharedPreferences preferences = App.getAppContext().getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_MULTI_PROCESS);
        preferences.edit().putString(DEFAULT_KEY, value).commit();
    }

    @SuppressLint("InlinedApi")
    public static String getValue() {
        SharedPreferences preferences = App.getAppContext().getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_MULTI_PROCESS);
        return preferences.getString(DEFAULT_KEY, null);
    }

    public static void remove(String key) {
        SharedPreferences preferences = App.getAppContext().getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor ed = preferences.edit();
        ed.remove(key);
        ed.commit();
    }

    @SuppressLint("InlinedApi")
    public static void ensureValue(boolean isClear) {
        if (isClear) {
            values = null;
        }
        if (null == values) {
            SharedPreferences preferences = App.getAppContext().getSharedPreferences(DEFAULT_PREFERENCE, Context.MODE_MULTI_PROCESS);
            String value = preferences.getString(DEFAULT_KEY, null);
            if (!TextUtils.isEmpty(value)) {
                values = value.split("\\|");
            } else {
//				initPreference(preferences);
                ensureValue(false);
            }
        }
    }

    /**
     * 生成取值 默认值 -1;
     *
     * @param allValues
     */
    private static void createValue(String[] allValues) {
        String stringValue = new String();
        for (int i = 0; i < allValues.length; i++) {
            if (allValues.length - 1 != i) {
                stringValue += allValues[i] + "|";
            } else {
                stringValue += allValues[i];
            }
        }
        setValue(stringValue);
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static int getInt(boolean isClear, int index) {
        ensureValue(isClear);
        int intValue = -1;
        if (index < values.length) {
            String value = values[index];
            if (!TextUtils.isEmpty(value)) {
                try {
                    intValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    intValue = -1;
                }
            }
        }
        return intValue;
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static int getInt(int index) {
        return getInt(false, index);
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static long getLong(boolean isClear, int index) {
        ensureValue(isClear);
        long intValue = -1;
        if (index < values.length) {
            String value = values[index];
            if (!TextUtils.isEmpty(value)) {
                try {
                    intValue = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    intValue = -1;
                }
            }
        }
        return intValue;
    }

    /**
     * 根据角标获取 默认值 -1;
     *
     * @param index
     * @return
     */
    public static long getLong(int index) {
        return getLong(false, index);
    }

    /**
     * 获得boolean值
     *
     * @param index
     * @return
     */
    public static boolean getBoolean(boolean isClear, int index) {
        return 1 == getInt(isClear, index);
    }

    /**
     * 获得boolean值
     *
     * @param index
     * @return
     */
    public static boolean getBoolean(int index) {
        return 1 == getInt(false, index);
    }

    /**
     * 取反boolean值
     *
     * @param index 默认值
     * @return
     */
    public static boolean getRvsBoolean(boolean isClear, int index) {
        return 1 != getInt(isClear, index);
    }

    /**
     * 取反boolean值
     *
     * @param index 默认值
     * @return
     */
    public static boolean getRvsBoolean(int index) {
        return 1 != getInt(false, index);
    }


    /**
     * 根据角标获取 默认值 null;
     *
     * @param index
     * @return
     */
    public static String getString(boolean isClear, int index) {
        ensureValue(isClear);
        String stringValue = null;
        if (index < values.length) {
            stringValue = values[index];
            if (DEFAULT_VALUE.equals(stringValue)) {
                stringValue = null;
            }
        }
        return stringValue;
    }

    /**
     * 根据角标获取 默认值 null;
     *
     * @param index
     * @return
     */
    public static String getString(int index) {
        return getString(false, index);
    }

    public static void setInt(int index, int value) {
        ensureValue(false);
        values[index] = String.valueOf(value);
        createValue(values);
    }

    public static void setLong(int index, long value) {
        ensureValue(false);
        values[index] = String.valueOf(value);
        createValue(values);
    }

    public static void setString(int index, String value) {
        ensureValue(false);
        values[index] = !TextUtils.isEmpty(value) ? value : DEFAULT_VALUE;
        createValue(values);
    }

    public static void setBoolean(int index, Boolean value) {
        ensureValue(false);
        values[index] = String.valueOf((value ? 1 : -1));
        createValue(values);
    }

    /**
     * 获得设备唯一id
     *
     * @return
     */
    public static String getDid() {
        if (TextUtils.isEmpty(getString(false, ConfigName.DEVICE_ID))) {
            DeviceUtils.initID(App.getAppContext());
        }
        return getString(false, ConfigName.DEVICE_ID);
    }
}
