package com.example.demo.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock声明为局部变量，使用lock
 * 我们会看到这样的执行结果，如果结果不是这样的，可以把循环加长一些就可以看到效果了。
 * 这是因为每个线程都new出了一个锁，因此他们互不影响，没有满足线程安全的需求。
 */
public class TestReentrantLock1 {

    private ArrayList<Integer> arrayList = new ArrayList<>();

    public void insert(Thread thread) {
        Lock lock = new ReentrantLock();    //注意这个地方
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i = 0; i <1000; i++) {
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
        final TestReentrantLock1 test = new TestReentrantLock1();

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
