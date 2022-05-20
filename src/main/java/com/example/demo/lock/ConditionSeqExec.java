package com.example.demo.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Condition实现顺序执行
 */
public class ConditionSeqExec {

    private volatile static int nextPrintWho = 1;
    private static ReentrantLock lock = new ReentrantLock();
    private static final Condition conditionA = lock.newCondition();
    private static final Condition conditionB = lock.newCondition();
    private static final Condition conditionC = lock.newCondition();
    public static void main(String[] args) {
        Thread threadA = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    //如果nextPrintWho不为1就等待
                    while (nextPrintWho != 1) {
                        conditionA.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.print("ThreadA " + (i + 1)+"       " );
                    }
                    System.out.println();
                    nextPrintWho = 2;
                    //通知conditionB实例的线程运行
                    conditionB.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread threadB = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    //如果nextPrintWho不为2就等待
                    while (nextPrintWho != 2) {
                        conditionB.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.print("ThreadB " + (i + 1)+"       " );
                    }
                    System.out.println();
                    nextPrintWho = 3;
                    //通知conditionC实例的线程运行
                    conditionC.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread threadC = new Thread() {
            public void run() {
                try {
                    lock.lock();
                    //如果nextPrintWho不为3就等待
                    while (nextPrintWho != 3) {
                        conditionC.await();
                    }
                    for (int i = 0; i < 3; i++) {
                        System.out.print("ThreadC " + (i + 1)+"       " );
                    }
                    System.out.println();
                    nextPrintWho = 1;
                    //通知conditionA实例的线程运行
                    conditionA.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread[] aArray = new Thread[5];
        Thread[] bArray = new Thread[5];
        Thread[] cArray = new Thread[5];
        for (int i = 0; i < 5; i++) {
            aArray[i] = new Thread(threadA);
            bArray[i] = new Thread(threadB);
            cArray[i] = new Thread(threadC);
            aArray[i].start();
            bArray[i].start();
            cArray[i].start();
        }
    }

}
