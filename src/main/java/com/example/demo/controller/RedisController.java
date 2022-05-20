package com.example.demo.controller;

import com.example.demo.entity.Users;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Api(value = "redis", tags = "Redis管理")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/testString")
    public String testString()  {
        stringRedisTemplate.opsForValue().set("name", "louis");
        String name = stringRedisTemplate.opsForValue().get("name");
        return "the value of key 'name' is : " + name ;
    }

    @GetMapping("/testObject")
    public String testObject()  {
        StringBuilder result = new StringBuilder();
        Users users = new Users("louis", "123456");
        ValueOperations<String, Users> operations = redisTemplate.opsForValue();
        operations.set("sys.user", users);
        operations.set("sys.user.timeout", users, 1, TimeUnit.SECONDS);    // 设置1秒后过期
        result.append("过期前：").append("\n");
        result.append("sys.user=" + operations.get("sys.user")).append("\n");
        result.append("sys.user.timeout=" + operations.get("sys.user.timeout"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.append("\n").append("过期后：").append("\n");
        result.append("sys.user=" + operations.get("sys.user")).append("\n");
        result.append("sys.user.timeout=" + operations.get("sys.user.timeout"));
        return result.toString();
    }

    @GetMapping("/getUser")
    @Cacheable(value="user", key="#id")
    public Users getUser(Integer id) {
        Users user = new Users("louis", "123456");
        System.out.println("用户对象缓存不存在，返回一个新的用户对象。");
        return user;
    }

    @GetMapping("/updUser")
    @CachePut(value="user", key="#id")
    public Users updUser(Integer id) {
        Users user = new Users("louis-1", "123456-1");
        System.out.println("用户对象缓存已被更新。");
        return user;
    }

    @GetMapping("/delUser")
    @CacheEvict(value="user", key="#id")
    public boolean delUser(Integer id) {
        System.out.println("用户对象缓存已被删除");
        return true;
    }

    @GetMapping("/test1")
    public void test1() {
        try {
            //由于 enableTransactionSupport 属性的默认值是 false，
            //导致了每一个 RedisConnection 都是重新获取的。
            //所以执行的 MULTI 和 EXEC 这两个命令不在同一个 Connection 中。
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForValue().set("name", "qinyi");
            stringRedisTemplate.opsForValue().set("gender", "male");
            stringRedisTemplate.opsForValue().set("age", "19");
            System.out.println(stringRedisTemplate.exec());
        } catch (Throwable e) {
            stringRedisTemplate.discard();
        }
    }

    @GetMapping("/test2")
    public void test2() {
        try {
            //开启事务支持，在同一个 Connection 中执行命令（适用spring-boot-starter-data-redis 2.1.8版本）
            stringRedisTemplate.setEnableTransactionSupport(true);
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForValue().set("name", "qinyi");
            stringRedisTemplate.opsForValue().set("gender", "male");
            stringRedisTemplate.opsForValue().set("age", "19");
            System.out.println(stringRedisTemplate.exec());
        } catch (Throwable e) {
            stringRedisTemplate.discard();
        } finally {
            stringRedisTemplate.setEnableTransactionSupport(false);
        }
    }

    @GetMapping("/test3")
    public void test3() {
        //通过使用 SessionCallback，该接口保证其内部所有操作都是在同一个Session中
        try {
            SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    stringRedisTemplate.opsForValue().set("name", "qinyi");
                    stringRedisTemplate.opsForValue().set("gender", "male");
                    stringRedisTemplate.opsForValue().set("age", "19");
                    return operations.exec();
                }
            };
            stringRedisTemplate.execute(sessionCallback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
