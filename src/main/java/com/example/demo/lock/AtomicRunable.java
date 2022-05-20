package com.example.demo.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger的简单使用
 * AtomicInteger与int的区别。
 * 这个例程是1000个线程，每个线程都对同一个参数做10000次++的操作。
 * 从结果上可以看出，使用AtomicInteger之后，无论执行多少次，每次都能够保证原子性。
 * 因此Atomic实现了和lock、synchronized同样的目的。
 */
public class AtomicRunable implements Runnable {

    private static int count;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            count++;
            atomicInteger.incrementAndGet();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];
        Runnable runnable = new AtomicRunable();
        for (int i = 0; i < 1000; i++) {
            threads[i] = new Thread(runnable) ;
            threads[i].start();
        }
        Thread.sleep(3000);
        System.out.println(count);
        System.out.println(atomicInteger);
    }

}
