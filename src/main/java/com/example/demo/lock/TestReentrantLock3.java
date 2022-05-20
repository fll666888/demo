package com.example.demo.lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * trylock的使用
 * 只有在调用时才可以获得锁。如果可用，则获取锁定，并立即返回值为true；如果锁不可用，则此方法将立即返回值为false 。
 * 这个例程可以看到，线程1在尝试获取锁的时候由于，线程0正在使用，因此尝试失败了。
 */
public class TestReentrantLock3 {

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    public void insert(Thread thread) {
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i = 0; i < 100000; i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"获取锁失败");
        }
    }

    public static void main(String[] args)  {
        final TestReentrantLock3 test = new TestReentrantLock3();

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
