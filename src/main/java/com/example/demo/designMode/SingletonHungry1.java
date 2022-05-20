package com.example.demo.designMode;

/**
 * 饿汉式（静态变量）
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
public class SingletonHungry1 {

    private static SingletonHungry1 instance = new SingletonHungry1();

    private SingletonHungry1 (){

    }

    public static SingletonHungry1 getInstance() {
        return instance;
    }

}
