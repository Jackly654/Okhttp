package daimamiao.com.myokhttp.listener;

import daimamiao.com.myokhttp.utils.HandleTask;

/**
 * Created by pengying on 2015/8/18.
 */
public class SimpleTaskAction<T> implements HandleTask.TaskAction<T> {

    private HandleTask.TaskAction<T> action;
    public SimpleTaskAction(HandleTask.TaskAction<T> action) {
        this.action = action;
    }


    @Override
    public T run() {
        return null;
    }

    @Override
    public void postRun(T t) {

    }
}
