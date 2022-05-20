package com.example.demo.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock声明为类的属性，使用lock
 * 获得锁。如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到获取锁。
 * 当Lock申明为类属性的时候，两个线程只创建一个锁，因此是同一把锁，可以满足线程安全的需求。
 */
public class TestReentrantLock2 {

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    public void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i = 0; i<1000; i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }

    public static void main(String[] args)  {
        final TestReentrantLock2 test = new TestReentrantLock2();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
    }

}
