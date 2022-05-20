package com.example.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis分布式锁
 * redis中的setnx和get、set方法，这两个方法在redisTemplate分别是setIfAbsent和getAndSet方法，实现线程安全；
 * 因为redis是单线程，能保证线程的安全性，而且redis强大的读写能力能提高效率。
 */
@Component
public class DistributedLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value
     * @return
     */
    public boolean lock(String key, String value) {
        //如果不存在则设置，存在则不改变原来的值
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            //可以成功设置，也就是key不存在
            return true;
        }
        /**
         * 判断锁超时，防止原来的操作异常，没有进行解锁操作而导致死锁
         */
        //获取当前key的值
        String currentValue  = (String) redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间value，并设置新的锁时间value
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            //假设两个线程同时进来这里，因为key被占用了，而且锁过期了。获取的值 currentValue = A(get取的旧的值肯定是一样的)；
            //两个线程的value都是B，锁时间已经过期了，而这里面的 getAndSet 一次只会一个执行，也就是一个执行之后，上一个的value已经变成了B。
            //只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
            if (oldValue != null && oldValue.equals(currentValue)) {
                //oldValue不为空且oldValue等于currentValue，也就是校验是不是上个对应的时间戳，也是防止并发
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if(currentValue != null && currentValue.equals(value)){
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
