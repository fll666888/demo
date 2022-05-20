package com.example.demo.designMode;

/**
 * 饿汉式（静态代码块）
 *
 * 是否 Lazy 初始化：否
 *
 * 是否多线程安全：是
 *
 * 实现难度：易
 *
 * 描述：这种方式比较常用，但如果对象一直没有被调用，就容易产生垃圾对象。
 * 优点：没有加锁，执行效率会提高。
 * 缺点：类加载时就初始化，浪费内存。
 */
public class SingletonHungry2 {

    private static SingletonHungry2 instance;

    static {
        instance = new SingletonHungry2();
    }

    private SingletonHungry2(){

    }

    public static SingletonHungry2 getInstance() {
        return instance;
    }

}
