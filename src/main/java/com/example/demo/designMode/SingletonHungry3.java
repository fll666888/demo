package com.example.demo.designMode;

/**
 * 饿汉式（枚举）
 *
 * 是否 Lazy 初始化：否
 *
 * 是否多线程安全：是
 *
 * 实现难度：易
 *
 * 自动支持序列化机制
 * 防止反序列化、反射机制重新创建新的对象
 */
public enum SingletonHungry3 {

    INSTANCE;

    //其他需要的方法
    public void doSomething() {

    }

}
