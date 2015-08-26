package daimamiao.com.myokhttp.listener;

/**
 * Created by pengying on 2015/8/26.
 * 可变参数的任务，为了解析xml的属性
 */
public interface Itask<T> {
    void run(T... ts);
}
