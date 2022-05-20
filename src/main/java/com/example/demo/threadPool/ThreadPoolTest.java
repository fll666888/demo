package com.example.demo.threadPool;

import java.util.concurrent.*;

/**
 * 1    corePoolSize	int	核心线程池大小
 * 2	maximumPoolSize	int	最大线程池大小
 * 3	keepAliveTime	long 线程最大空闲时间
 * 4	unit	TimeUnit	时间单位
 * 5	workQueue	BlockingQueue<Runnable>	线程等待队列
 * 6	threadFactory	ThreadFactory	线程创建工厂
 * 7	handler	RejectedExecutionHandler	拒绝策略
 */
public class ThreadPoolTest {

    /**
     * 创建一个可缓存线程池程，如果当前没有可用线程，在执行结束后缓存60s，如果不被调用则移除线程。
     * 调用execute()方法时可以重用缓存中的线程。适用于很多短期异步任务的环境，可以提高程序性能。
     */
    ExecutorService cachelThreadPool = Executors.newCachedThreadPool();

    /**
     * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     */
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    /**
     * 创建一个周期性执行任务的线程池。
     */

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务都是按照指定的顺序(FIFO，LIFO，优先级)执行。
     */
    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    ExecutorService executorService = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000));

}
