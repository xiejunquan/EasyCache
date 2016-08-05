package com.ecache;

import com.alibaba.fastjson.JSON;

/**
 * 远程缓存
 * @author 谢俊权
 * @create 2016/7/9 16:31
 */
public class RemoteCache extends AbstractCache{

    private CacheInterface cacheInterface;

    public RemoteCache(CacheInterface cacheInterface) {
        super();
        this.cacheInterface = cacheInterface;
    }

    public RemoteCache(CacheConfig config, CacheInterface cacheInterface) {
        super(config);
        this.cacheInterface = cacheInterface;
    }

    @Override
    public <T> T set(String key, T value, int expiredSeconds) {
        String json =JSON.toJSONString(value);
        cacheInterface.set(key, json, expiredSeconds);
        return value;
    }

    public <T> T get(String key, RemoteCacheType type){
        CachePolicy<T> cachePolicy = cachePolicyRegister.get(key);
        MissCacheHandler<T> handler = (cachePolicy == null) ? null : cachePolicy.getMissCacheHandler();
        int expiredSeconds = (cachePolicy == null) ? cacheConfig.getDefaultExpiredSeconds() : cachePolicy.getExpiredSeconds();
        return get(key, expiredSeconds, type, handler);
    }

    @Override
    public <T> T get(String key, int expiredSeconds, Class<T> clazz, MissCacheHandler<T> handler) {
        return get(key, expiredSeconds, new RemoteCacheType<T>(){}, handler);
    }

    public <T> T get(String key, int expiredSeconds, RemoteCacheType type, MissCacheHandler<T> handler) {
        String result = cacheInterface.get(key);
        if(handler != null){
            if(result == null){
                if(cacheConfig.isAvoidServerOverload()){
                    lock(key);
                    try{
                        result = cacheInterface.get(key);
                        if(result == null){
                            return set(key, handler.getData(), expiredSeconds);
                        }
                    }finally {
                        unlock(key);
                    }
                }else{
                    return set(key, handler.getData(), expiredSeconds);
                }
            }
        }
        return JSON.parseObject(result, type.type);
    }
}
