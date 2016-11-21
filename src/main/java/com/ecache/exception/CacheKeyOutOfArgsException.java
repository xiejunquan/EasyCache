package com.ecache.exception;

/**
 * bean容器中没有找到内置缓存对象
 *
 * @author xiejunquan
 * @create 2016/11/21 15:30
 */
public class CacheKeyOutOfArgsException extends RuntimeException{

    public CacheKeyOutOfArgsException(String message) {
        super(message);
    }

    public CacheKeyOutOfArgsException(String message, Throwable cause) {
        super(message, cause);
    }
}
