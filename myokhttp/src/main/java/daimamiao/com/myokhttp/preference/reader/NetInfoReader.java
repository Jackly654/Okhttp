package daimamiao.com.myokhttp.preference.reader;

import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

import daimamiao.com.myokhttp.listener.ITask;
import daimamiao.com.myokhttp.preference.config.NetConfig;
import daimamiao.com.myokhttp.utils.XmlParser;

/**
 * 网络信息读取类
 * Created by pengying on 2015/8/26.
 */
public class NetInfoReader extends AssetReader<NetConfig>{



    @Override
    public XmlParser.ParserListener getParserListener(final HashMap<String, NetConfig> configs) {
        return new XmlParser.ParserListener() {
            private NetConfig config;
            @Override
            public void startParset(XmlPullParser parser) {
                if("item".equals(parser.getName())){
                    config = new NetConfig();
                    XmlParser.runParser(parser,new ITask<String>() {
                        @Override
                        public void run(String... ts) {
                            if ("action".equals(ts[0])) {
                                config.action = ts[1];
                            } else if ("method".equals(ts[0])) {
                                config.method = ts[1];
                            } else if ("params".equals(ts[0])) {
                                config.params = !TextUtils.isEmpty(ts[1]) ? ts[1].split("&") : null;
                            } else if ("url".equals(ts[0])) {
                                config.url = ts[1];
                            } else if ("info".equals(ts[0])) {
                                config.info = ts[1];
                            } else if ("tag".equals(ts[0])) {
                                config.tag = ts[1];
                            } else if ("filter".equals(ts[0])) {
                                config.filter = Boolean.valueOf(ts[1]);
                            } else if ("addex".equals(ts[0])) {
                                config.addExtras = Boolean.valueOf(ts[1]);
                            } else if ("file".equals(ts[0])) {
                                config.files = !TextUtils.isEmpty(ts[1]) ? ts[1].split("&") : null;
                            } else if ("replease".equals(ts[0])) {
                                config.replease = Boolean.valueOf(ts[1]);
                            } else if ("execute".equals(ts[0])) {
                                config.execute = Boolean.valueOf(ts[1]);
                            } else if ("version".equals(ts[0])) {
                                config.version = Integer.valueOf(ts[1]);
                            }

                        }
                    });
                }
            }

            @Override
            public void endParset(XmlPullParser parser) {
                if("item".equals(parser.getName())&&config!=null){
                    configs.put(config.action,config);
                }
            }
        };
    }


    @Override
    public String getConfig() {
        return "config/net_config.xml";
    }
}
