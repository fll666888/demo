package com.example.demo.lock;

/**
 * 使用多个Condition实例实现等待/通知机制
 * 从结果上可以看出只有A线程被唤醒了。明显的signalAll()与notifyAll()不一样，它没有唤醒全部wait()的线程。
 */
public class UseMoreConditionWaitNotify {

    public static class ThreadA extends Thread {
        private MyserviceMoreCondition service;
        public ThreadA(MyserviceMoreCondition service) {
            super();
            this.service = service;
        }
        @Override
        public void run() {
            service.awaitA();
        }
    }

    public static class ThreadB extends Thread {
        private MyserviceMoreCondition service;
        public ThreadB(MyserviceMoreCondition service) {
            super();
            this.service = service;
        }
        @Override
        public void run() {
            service.awaitB();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyserviceMoreCondition service = new MyserviceMoreCondition();
        ThreadA a = new ThreadA(service);
        a.setName("A");
        a.start();
        ThreadB b = new ThreadB(service);
        b.setName("B");
        b.start();
        Thread.sleep(3000);
        service.signalAll_A();
        //service.signalAll_B();
    }

}
