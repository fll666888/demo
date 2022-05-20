package com.example.demo.lock;

/**
 * 把 synchronized 关键字加在普通方法上,使用两个不同的对象调用。
 *
 * 可以看到，此时的结果和使用一个对象调用方法，且没有加锁运行的结果是一样的。
 */
public class TestSyn3 {

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
        TestSyn3 ts = new TestSyn3();
        TestSyn3 ts2 = new TestSyn3();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ts.test2();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ts2.test2();
            }
        }).start();
    }

}
