package daimamiao.com.okhttp;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.Map;

import daimamiao.com.myokhttp.HttpManager;
import daimamiao.com.myokhttp.NetWorkConfig;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    //HttpManager
    public void test() throws Exception{
        HttpManager.request(this, NetWorkConfig.source(),new HttpManager.ResponseParamsListener(){

            @Override
            public void onSuccess(boolean isScuuess, int code, Map<String, String> params, String result) {
                Map<String, String> params1 = params;
            }

            @Override
            public void onFail(boolean noNetwok, Exception error) {

            }
        });
    }
}