package daimamiao.com.myokhttp.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import daimamiao.com.myokhttp.listener.ITask;

/**
 * 解析XML内容
 * Created by pengying on 2015/8/26.
 */
public class XmlParser {
    /**
     * 开始解析
     */
    public static void startParser(InputStream inputStream,ParserListener listener){
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream,"utf-8");
            int eventType = parser.getEventType();
            while(XmlPullParser.END_DOCUMENT!= eventType){
                if(listener!=null){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            listener.startParset(parser);
                            break;
                        case XmlPullParser.END_TAG:
                            listener.endParset(parser);
                            break;
                        default:
                            break;
                    }
                }
                    eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeStream(inputStream);
        }

    }

    public static void runParser(XmlPullParser parser,ITask<String> task){

    }


    public interface ParserListener{
        void startParset(XmlPullParser parser);

        void endParset(XmlPullParser parser);
    }

    public interface ParserResultListener<E>{
        void parserResult(E e);
    }
}
