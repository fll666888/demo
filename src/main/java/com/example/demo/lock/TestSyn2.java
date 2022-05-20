package com.example.demo.lock;

/**
 * 把 synchronized 关键字加在普通方法上，使用同一个对象调用。
 *
 * 此时我们可以看到加锁之后，同一个对象中，枷锁的方式，两个线程同时要使用时，需要等前一个线程使用结束后才可以执行这个方法。
 */
public class TestSyn2 {

    /**
     * 加锁的普通方法
     */
    public synchronized void test2() {
        System.out.println("普通加锁方法执行");
        try {
            Thread.sleep(2000);//延时使得方法没有那么快运行结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("普通加锁方法结束");
    }

    public static void main(String[] args) {
        TestSyn2 ts = new TestSyn2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ts.test2();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ts.test2();
            }
        }).start();
    }

}
