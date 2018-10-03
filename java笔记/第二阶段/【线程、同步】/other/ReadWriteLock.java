package com.itheima.other;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(); -- 读写锁

     读锁：共享锁，共享锁和共享锁是可以并发的，共享锁和排他锁是互斥的
     写锁：排他锁，排他锁和共享锁及排他锁都是互斥
 */
public class ReadWriteLock {
    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        Thread thread = new Thread() {
            @Override
            public void run() {
                reentrantReadWriteLock.writeLock().lock(); //获取写锁，执行锁定
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程1 执行了。。。");
                reentrantReadWriteLock.writeLock().unlock();//获取写锁，解除锁定
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY); //设置线程的优先级，优先级越高，抢夺cpu的执行权概率越大
        thread.start();
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                reentrantReadWriteLock.readLock().lock(); //获取读锁，执行锁定
                System.out.println("线程2 执行了。。。");
                reentrantReadWriteLock.readLock().unlock(); //获取读锁，解除锁定
            }
        };
        thread1.setPriority(Thread.MIN_PRIORITY);
        thread1.start();
    }
}
