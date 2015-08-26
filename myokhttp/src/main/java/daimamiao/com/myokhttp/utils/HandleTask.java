package daimamiao.com.myokhttp.utils;

import daimamiao.com.myokhttp.listener.SimpleTaskAction;

/**
 * Created by pengying on 2015/8/18.
 */
public class HandleTask {
    /**
     * 任务执行回执
     *
     * @param <T> 数据
     * @author momo
     */
    public interface TaskAction<T> {
        /**
         * 子线程任务
         */
        T run();

        /**
         * 子线程执行完任务
         *
         * @param t
         */
        void postRun(T t);
    }

    public static <T> void run(final TaskAction<T> action) {
        RunnableUtils.runWithExecutor(new Runnable() {
            @Override
            public void run() {
                SimpleTaskAction<T> taskAction = new SimpleTaskAction<T>(action);
                taskAction.postRun(taskAction.run());
            }
        });
    }
}
