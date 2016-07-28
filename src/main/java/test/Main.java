package test;

import com.bean.CacheBeanFactory;
import com.cache.CacheConfig;
import com.cache.LocalCache;
import com.cache.RemoteCache;
import com.cache.RemoteCacheInterface;
import com.proxy.CacheInterceptor;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 谢俊权
 * @create 2016/7/9 18:54
 */
public class Main {

    public static void main(String[] args) throws Exception {

        CacheConfig config = new CacheConfig(60*60, 32, 2000);
//
        final AtomicInteger localExpireNumber = new AtomicInteger(0);
        final AtomicInteger localIntervalNumber = new AtomicInteger(0);
        LocalCache localCache = new LocalCache(config);
//        localCache.clearScheduler();
//        /*注册过期缓存策略*/
//        localCache.regist("local-expire-key", new CachePloy(10, new MissCacheHandler<MyValue>() {
//            @Override
//            public MyValue getData() {
//                return new MyValue(localExpireNumber.incrementAndGet(), "local-expire-value");
//            }
//        }));
//        /*注册定时刷新缓存策略*/
//        localCache.regist("local-interval-key", new CachePloy(0, 2, new MissCacheHandler<YourValue>() {
//            @Override
//            public YourValue getData() {
//                return new YourValue(localIntervalNumber.incrementAndGet(), "local-interval-value");
//            }
//        }));
//        Thread.sleep(1000 * 5);
//        MyValue localExpireMyValue = localCache.get("local-expire-key", MyValue.class);
//        System.out.println(localExpireMyValue.getId());
//        Thread.sleep(1000 * 5);
//        localExpireMyValue = localCache.get("local-expire-key", MyValue.class);
//        System.out.println(localExpireMyValue.getId());
//        Thread.sleep(1000 * 5);
//        YourValue localIntervalMyValue = localCache.get("local-interval-key", YourValue.class);
//        System.out.println(localIntervalMyValue.getId());
//        Thread.sleep(1000 * 5);
//        localIntervalMyValue = localCache.get("local-interval-key", YourValue.class);
//        System.out.println(localIntervalMyValue.getId());
//        /*即时获取缓存*/
//        int myId = 1024;
//        System.out.println(localCache.get("local-site-key", 100, MyValue.class, new MissCacheHandler<MyValue>(myId) {
//            @Override
//            public MyValue getData() {
//                return new MyValue((Integer) this.params, "local-site-value");
//            }
//        }).getId());
//
//
//        final AtomicInteger remoteExpireNumber = new AtomicInteger(0);
//        final AtomicInteger remoteIntervalNumber = new AtomicInteger(0);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(20);
        jedisPoolConfig.setMaxWaitMillis(1000*5);
        RemoteCacheInterface remoteCacheInterface = new RedisCache(jedisPoolConfig, "127.0.0.1", 6380, 1000*5);
        RemoteCache remoteCache = new RemoteCache(config, remoteCacheInterface);
//        /*注册过期缓存策略*/
//        remoteCache.regist("remote-expire-key", new CachePloy(10, new MissCacheHandler<MyValue>() {
//            @Override
//            public MyValue getData() {
//                return new MyValue(remoteExpireNumber.incrementAndGet(), "remote-expire-value");
//            }
//        }));
//        /*注册定时刷新缓存策略*/
//        remoteCache.regist("remote-interval-key", new CachePloy(0, 2, new MissCacheHandler<YourValue>() {
//            @Override
//            public YourValue getData() {
//                return new YourValue(remoteIntervalNumber.incrementAndGet(), "remote-interval-value");
//            }
//        }));
//
//
//        Thread.sleep(1000 * 5);
//        MyValue remoteExpireMyValue = remoteCache.get("remote-expire-key", MyValue.class);
//        System.out.println(remoteExpireMyValue.getId());
//        Thread.sleep(1000 * 5);
//        remoteExpireMyValue = remoteCache.get("remote-expire-key", MyValue.class);
//        System.out.println(remoteExpireMyValue.getId());
//
//        Thread.sleep(1000 * 5);
//        YourValue remoteIntervalMyValue = remoteCache.get("remote-interval-key", YourValue.class);
//        System.out.println(remoteIntervalMyValue.getId());
//        Thread.sleep(1000 * 5);
//        remoteIntervalMyValue = remoteCache.get("remote-interval-key", YourValue.class);
//        System.out.println(remoteIntervalMyValue.getId());
//
//        /*即时获取缓存*/
//        System.out.println(remoteCache.get("remote-site-key", 100, MyValue.class, new MissCacheHandler<MyValue>(myId) {
//            @Override
//            public MyValue getData() {
//                return new MyValue((Integer) this.params, "remote-site-value");
//            }
//        }).getId());


        CacheBeanFactory cacheBeanFactory = new CacheBeanFactory();
        cacheBeanFactory.set(localCache.getClass(), localCache);
        cacheBeanFactory.set(remoteCache.getClass(), remoteCache);
        CacheInterceptor cacheInterceptor = new CacheInterceptor(cacheBeanFactory);
        cacheInterceptor.run("test");

        UserService userService = cacheBeanFactory.get(UserService.class);
        String i = userService.getUserName(1);
        System.out.println(i);
        Thread.sleep(1000 * 1);
        i = userService.getUserName(1);
        System.out.println(i);
        Thread.sleep(1000 * 1);
        i = userService.getUserName(3);
        System.out.println(i);

        boolean successful = userService.login(new UserInfo(1, "123"));
        System.out.println(successful);
        Thread.sleep(1000 * 1);
        successful = userService.login(new UserInfo(1, "123"));
        System.out.println(successful);
        Thread.sleep(1000 * 1);
        successful = userService.login(new UserInfo(2, "234"));
        System.out.println(successful);
        Thread.sleep(1000 * 1);

    }
}