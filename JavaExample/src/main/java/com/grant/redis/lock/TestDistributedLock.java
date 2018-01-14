package com.grant.redis.lock;

/**
 * Created by Grant on 2017/12/14/014.
 */

public class TestDistributedLock {
    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 50; i++) {
            ThreadA threadA = new ThreadA(service);
            threadA.start();
        }
    }
    public static class ThreadA extends Thread {
        private Service service;

        public ThreadA(Service service) {
            this.service = service;
        }

        @Override
        public void run() {
            service.seckill();
        }
    }
}

