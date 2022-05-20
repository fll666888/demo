package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.CountDownLatch;

public class LatchTest {

    public static void main(String[] args) throws InterruptedException {
        Runnable taskTemp = new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("goodsId", 1);
                jsonObject.put("goodsNumber", 1);
                String res = HttpClientUtil.doPost_json("http://127.0.0.1:18080/demo/order", jsonObject.toJSONString());
                System.out.println(System.nanoTime() + " [" + Thread.currentThread().getName() + "] " + res);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        LatchTest latchTest = new LatchTest();
        latchTest.startTaskAllInOnce(100, taskTemp);
    }

    public long startTaskAllInOnce(int threadNums, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        for(int i = 0; i < threadNums; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        // 使线程在此等待，当开始门打开时，一起涌入门中
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            // 将结束门减1，减到0时，就可以开启结束门了
                            endGate.countDown();
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            t.start();
        }
        long startTime = System.nanoTime();
        System.out.println(startTime + " [" + Thread.currentThread() + "] All thread is ready, concurrent going...");
        // 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        System.out.println(endTime + " [" + Thread.currentThread() + "] All thread is completed.");
        return endTime - startTime;
    }

}
