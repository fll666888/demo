package com.example.demo.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读读共享
 * 两个读共享锁
 */
public class ReadRead {

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"正在进行读操作");
            }
            System.out.println(thread.getName()+"读操作完毕");
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            rwl.readLock().unlock();
        }
    }

    public static void main(String[] args)  {
        final ReadRead test = new ReadRead();
        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();
    }

}
