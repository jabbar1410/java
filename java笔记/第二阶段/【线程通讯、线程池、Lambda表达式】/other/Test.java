package com.itheima.other;

/**
 * 三个线程，
 *  A线程执行完 -- 通知B线程执行
 *  B线程执行完 -- 通知C线程执行
 *  C线程执行完 -- 通知A线程执行
 */

public class Test {
    public static void main(String[] args) {
        Baozipu2 baozipu = new Baozipu2();

        new Producter(baozipu,"生产者1").start();
        new Producter(baozipu,"生产者2").start();

        new Consumer(baozipu,"消费者1").start();
        new Consumer(baozipu,"消费者2").start();
        new Consumer(baozipu,"消费者3").start();
    }
}
