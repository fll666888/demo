package com.example.demo.lock;

/**
 * 把 synchronized 关键字加在static方法上,使用两个不同的对象调用。
 *
 * 此时我们又可以看到，调用这个方法又需要等待了。
 * 这是因为，静态方法它是属于类本身的。所有对象共用这个方法，而不是每个对象各自拥有的。
 * 因此将它锁住，即使是其他对象，也需要等待它被释放锁的时候才可以使用。
 */
public class TestSyn4 {

    /**
     * 加锁的普通方法
     */
    public static synchronized void test2() {
        System.out.println("静态加锁方法执行");
        try {
            Thread.sleep(2000);//延时使得方法没有那么快运行结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("静态加锁方法结束");
    }

    public static void main(String[] args) {
        TestSyn4 ts = new TestSyn4();
        TestSyn4 ts2 = new TestSyn4();
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
