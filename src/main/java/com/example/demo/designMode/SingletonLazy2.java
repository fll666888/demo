package com.example.demo.designMode;

/**
 * 懒汉式（双检锁/双重校验锁）
 *
 * 是否 Lazy 初始化：是
 *
 * 是否多线程安全：是
 *
 * 实现难度：较复杂
 *
 * 描述：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。
 */
public class SingletonLazy2 {

    //加入volatile关键字修饰之后，会禁用指令重排。
    private volatile static SingletonLazy2 instance;

    private SingletonLazy2(){}

    public static SingletonLazy2 getInstance() {
        if (instance == null) {
            synchronized (SingletonLazy2.class) {
                if (instance == null) {
                    instance = new SingletonLazy2();
                }
            }
        }
        return instance;
    }

}
