package daimamiao.com.myokhttp;

import android.util.Pair;

import java.io.File;
import java.util.ArrayList;

import daimamiao.com.myokhttp.preference.config.NetConfig;

/**
 * Created by pengying on 2015/8/12.
 */
public interface HttpInterface {
    /**
     * 移除请求事件
     * TODO 待实现
     * @param action
     */
    void removeCall(String action);

    void call(NetConfig config,final HttpManager.ResponseParamsListener listener,Object[] params,File[] files);

    void call(NetConfig config,final HttpManager.ResponseListener listener,Object... params);

    void request(String url,int method,ArrayList<Pair<String,String>> params,HttpManager.ResponseParamsListener listener);
}
