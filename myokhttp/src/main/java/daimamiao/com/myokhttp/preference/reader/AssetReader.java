package daimamiao.com.myokhttp.preference.reader;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import daimamiao.com.myokhttp.utils.IOUtils;
import daimamiao.com.myokhttp.utils.XmlParser;
import daimamiao.com.myokhttp.utils.XmlParser.ParserListener;
import daimamiao.com.okhttp.application.App;

/**
 * Created by pengying on 2015/8/17.
 */
public abstract class AssetReader<T> {
    /**
     * initiation source
     */
    public HashMap<String,T> read(){
        InputStream inputStream = null;
        HashMap<String,T>configs = null;
        try {
            Context context = App.getAppContext();
            configs = new HashMap<String,T>();
            inputStream = context.getResources().getAssets().open(getConfig());
            if(inputStream!=null){
                ParserListener listener = getParserListener(configs);
                if(listener!=null){
                    XmlParser.startParser(inputStream,listener);
                }else{
                    throw new NullPointerException("listener不能为空");
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            //关闭IO流
            IOUtils.closeStream(inputStream);
        }
        return configs;
    }

    /**
     * 获得配置项位置
     *
     * @return
     */
    public abstract String getConfig();

    /**
     * 读取配置项
     */
    public abstract ParserListener getParserListener(HashMap<String,T>configs);
}
