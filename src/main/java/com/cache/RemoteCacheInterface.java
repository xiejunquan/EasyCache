package com.cache;

/**
 * @author 谢俊权
 * @create 2016/7/12 13:32
 */
public interface RemoteCacheInterface {

    void set(String key, String value, int expireSeconds);

    String get(String key);
}
