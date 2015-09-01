package daimamiao.com.myokhttp.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import daimamiao.com.okhttp.application.App;

/**功能描述：这是一个简易的Json-HashMap转换工具，可以将普通的json数据（字符串）
 *         转换为一个HashMap<Srting, Object>表格，也可以反过来操作。此外还支 持将json数据格式化。
 * Created by pengying on 2015/9/1.
 */
public class JsonUtls {
    private static final String LEFT_BOLCK = "[";
    private static final String RIGHT_BOLCK = "]";
    private static final String LEFT_MAX_BOLCK = "{";
    private static final String RIGHT_MAX_BOLCK = "}";

    private static final Gson gson = new Gson();

    private static final int JSON_OBJECT = 1;
    private static final int JSON_ARRAY = 2;
    private static final int NOT_A_JSON = 3; // 不是json

    /**
     * 将指定的json数据转成 HashMap<String, Object>对象
     */
    public static HashMap<String, Object> fromJson(String jsonStr) {
        try {
            if (jsonStr.startsWith(LEFT_BOLCK) && jsonStr.endsWith(RIGHT_BOLCK)) {
                jsonStr = "{\"fakelist\":" + jsonStr + RIGHT_MAX_BOLCK;
            }

            JSONObject json = new JSONObject(jsonStr);
            return fromJson(json);
        } catch (Throwable t) {
            //RecordManager.get().insertException(App.getAppContext(), ExceptionType.DATE_EXCEPTION, t);
        }
        return new HashMap<String, Object>();
    }

    private static HashMap<String, Object> fromJson(JSONObject json) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> iKey = json.keys();
        while (iKey.hasNext()) {
            String key = iKey.next();
            Object value = json.opt(key);
            if (JSONObject.NULL.equals(value)) {
                value = null;
            }
            if (value != null) {
                if (value instanceof JSONObject) {
                    value = fromJson((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = fromJson((JSONArray) value);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    private static ArrayList<Object> fromJson(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0, size = array.length(); i < size; i++) {
            Object value = array.opt(i);
            if (value instanceof JSONObject) {
                value = fromJson((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = fromJson((JSONArray) value);
            }
            list.add(value);
        }
        return list;
    }


    /**
     * 将指定的HashMap<String, Object>对象转成json数据
     */
    public static String fromHashMap(HashMap<String, Object> map) {
        try {
            return getJSONObject(map).toString();
        } catch (Throwable t) {
            //RecordManager.get().insertException(App.getAppContext(), ExceptionType.DATE_EXCEPTION, t);
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private static JSONObject getJSONObject(HashMap<String, Object> map) throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof HashMap<?, ?>) {
                value = getJSONObject((HashMap<String, Object>) value);
            } else if (value instanceof ArrayList<?>) {
                value = getJSONArray((ArrayList<Object>) value);
            }
            json.put(entry.getKey(), value);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (Object value : list) {
            if (value instanceof HashMap<?, ?>) {
                value = getJSONObject((HashMap<String, Object>) value);
            } else if (value instanceof ArrayList<?>) {
                value = getJSONArray((ArrayList<Object>) value);
            }
            array.put(value);
        }
        return array;
    }

    /**
     * 从字符串中获得map键值
     *
     * @param result
     * @return
     */
    public static Map<String, String> getResonseDataMap(String result) {
        Map<String, String> params = null;
        if (!TextUtils.isEmpty(result)) {
            try {
                params = new HashMap<>();
                JSONObject json = new JSONObject(result);
                Iterator<String> iKey = json.keys();
                while (iKey.hasNext()) {
                    String key = iKey.next();
                    Object value = json.opt(key);
                    if (JSONObject.NULL.equals(value)) {
                        value = null;
                    }
                    if (null != value && !TextUtils.isEmpty(value.toString())) {
                        params.put(key, value.toString());
                    }
                }
            } catch (Exception e) {
            }
        }
        return params;
    }

    /**
     * 获得字符串中数值
     *
     * @param value
     * @return
     */
    public static int getRequestNumber(String value) {
        int number = -1;
        if (!TextUtils.isEmpty(value)) {
            try {
                number = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                number = -1;
            }
        }
        return number;
    }
}
