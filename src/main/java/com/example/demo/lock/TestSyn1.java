package com.example.demo.lock;

/**
 * 没有使用锁时，同时运行同一个对象的普通方法效果如何
 *
 * 由此可见，两个线程的方法是同时在跑的。虽然运行的是同一个方法，但是第二个线程运行方法是没有等待第一个线程运行完方法就开始运行了。
 */
public class TestSyn1 {

    /**
     * 没有加锁的普通方法
     */
    public void test1() {
        System.out.println("普通方法执行");
        try {
            Thread.sleep(2000);//延时使得方法没有那么快运行结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("普通方法结束");
    }

    public static void main(String[] args) {
        TestSyn1 ts = new TestSyn1();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ts.test1();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ts.test1();
            }
        }).start();
    }

}
