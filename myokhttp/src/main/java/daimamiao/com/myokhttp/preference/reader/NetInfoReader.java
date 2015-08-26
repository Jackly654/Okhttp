package daimamiao.com.myokhttp.preference.reader;

import java.util.HashMap;

import daimamiao.com.myokhttp.preference.config.NetConfig;
import daimamiao.com.myokhttp.utils.XmlParser;

/**
 * 网络信息读取类
 * Created by pengying on 2015/8/26.
 */
public class NetInfoReader extends AssetReader<NetConfig>{



    @Override
    public XmlParser.ParserListener getParserListener(HashMap<String, NetConfig> configs) {
        return null;
    }


    @Override
    public String getConfig() {
        return "config/net_config.xml";
    }
}
