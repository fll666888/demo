package com.example.demo.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lockInterruptibly的使用
 * 获取锁，如果可用并立即返回。
 * 如果锁不可用，那么当前线程将被禁用以进行线程调度，并且处于休眠状态，和lock()方法不同的是在锁的获取中可以中断当前线程（相应中断）。
 */
public class TestReentrantLock4 {

    private Lock lock = new ReentrantLock();

    public void insert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();   //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {
            System.out.println(thread.getName()+"得到了锁");
            long startTime = System.currentTimeMillis();
            for(    ;     ;) {
                if(System.currentTimeMillis() - startTime >= 3999)
                    break;
                //插入数据
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            System.out.println(Thread.currentThread().getName()+"执行finally");
            lock.unlock();
            System.out.println(thread.getName()+"释放了锁");
        }
    }

    public static void main(String[] args)  {
        TestReentrantLock4 test = new TestReentrantLock4();
        MyThread thread1 = new MyThread(test);
        MyThread thread2 = new MyThread(test);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
    }
}

class MyThread extends Thread {
    private TestReentrantLock4 test = null;
    public MyThread(TestReentrantLock4 test) {
        this.test = test;
    }
    @Override
    public void run() {
        try {
            test.insert(Thread.currentThread());
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName()+"被中断");
        }
    }
}
