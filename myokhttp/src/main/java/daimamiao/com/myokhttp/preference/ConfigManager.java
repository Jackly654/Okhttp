package daimamiao.com.myokhttp.preference;

import java.util.HashMap;

import daimamiao.com.myokhttp.listener.Task;
import daimamiao.com.myokhttp.preference.config.Config;
import daimamiao.com.myokhttp.preference.reader.AssetReader;
import daimamiao.com.myokhttp.utils.HandleTask;

/**配置管理对象
 * Created by pengying on 2015/8/17.
 */
public class ConfigManager<T extends Config> {
    /**
     * 配置首发logo对象
     */
    private HashMap<String, HashMap<String, T>> mConfigs;

    private static final ConfigManager mConfigManager = new ConfigManager();

    private ConfigManager() {
        mConfigs = new HashMap<>();
    }

    public static final ConfigManager get() {
        return mConfigManager;
    }

    private HashMap<String, T> ensureConfig(AssetReader<T> reader) {
        String name = reader.getClass().getSimpleName();
        HashMap<String, T> configs = mConfigs.get(name);
        if (null == configs) {
            configs = reader.read();
            mConfigs.put(name, configs);
        }
        return configs;
    }

    /**
     * 在子线程中确定取值,执行执定任务
     *
     * @param key
     * @param reader
     * @param task
     */
    public <A extends AssetReader, T extends Config> void runTask(final String key, final A reader, final Task<T> task) {
        HandleTask.run(new HandleTask.TaskAction<T>() {
            @Override
            public T run() {
                HashMap<String, T> configs = (HashMap<String, T>) ensureConfig(reader);
                T t = null;
                if (null != configs) {
                    t = configs.get(key);
                }
                return t;
            }

            @Override
            public void postRun(T config) {
                if (null != task && null != config) {
                    task.run(config);
                }
            }
        });
    }


    /**
     * 获取splash配置项
     *
     * @param channel
     * @return
     */
    public void runLogoAction(String channel, Task<LogoConfig> task) {
        runTask(channel, new LogoConfigReader(), task);
    }

    /**
     * 获取网络配置项
     *
     * @param action
     * @return
     */
    public void runNetAction(String action, Task<T> task) {
        String key = NetInfoReader.class.getSimpleName();
        HashMap<String, T> config = mConfigs.get(key);
        if (null != config) {
            T t = config.get(action);
            if (null != t) {
                if (null != task) {
                    task.run(t);
                }
                return;
            }
        }
        runTask(action, new NetInfoReader(), task);
    }

   /* *//**
     * 获得统计ui信息
     *
     * @param object
     * @return
     *//*
    public void runUiAction(Object object, Task<T> task) {
        String action = object.getClass().getName();
        String key = CollectReader.class.getSimpleName();
        HashMap<String, T> config = mConfigs.get(key);
        if (null != config) {
            T t = config.get(action);
            if (null != t) {
                if (null != task) {
                    task.run(t);
                }
                return;
            }
        }
        runTask(action, new CollectReader(), task);
    }*/

    public void recycler() {
    }

}