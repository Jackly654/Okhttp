package daimamiao.com.myokhttp.utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import daimamiao.com.myokhttp.listener.Task;

/**
 * Created by pengying on 2015/8/14.
 */
public class RunnableUtils {
    private static final ExecutorService executorService;

    static{
        executorService = Executors.newCachedThreadPool();
    }

    public static void runWithExecutor(final Runnable runnable){
        runWithTryCatch(new Runnable() {

            @Override
            public void run() {
                if(null!= runnable){
                    executorService.execute(runnable);
                }
            }
        });

        }


    public static void runWithTryCatch(Runnable runnable){
        try {
            if(null!=runnable){
                runnable.run();
            }
        }catch (Exception e){
            //上传崩溃
        }
    }

    private static void runWithTryCatch(Task<Boolean> task) {
        try{
            if(null!=task){
                task.run(true);
            }
        }catch (Exception e){
            //上传崩溃
        }
    }
}
