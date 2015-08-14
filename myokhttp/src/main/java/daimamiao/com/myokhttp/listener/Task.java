package daimamiao.com.myokhttp.listener;

/**
 * Created by pengying on 2015/8/14.
 */
public interface Task<T> {
    void run(T t);
}
