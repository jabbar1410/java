package com.itheima.other;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程，
 *  A线程执行完 -- 通知B线程执行
 *  B线程执行完 -- 通知C线程执行
 *  C线程执行完 -- 通知A线程执行
 */

public class Test1 {
    static int runWho = 1; //1：A  2：B 3：C
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();
        Condition c3 = lock.newCondition();



        new Thread(){
            @Override
            public void run() {
                while (true){
                    lock.lock();
                    if (runWho != 1){
                        try {
                            c1.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("A运行");
                    runWho = 2;
                    c2.signal();
                    lock.unlock();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                while (true){
                    lock.lock();
                    if (runWho != 2){
                        try {
                            c2.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("B运行");
                    runWho = 3;
                    c3.signal();
                    lock.unlock();
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                while (true){
                    lock.lock();
                    if (runWho != 3){
                        try {
                            c3.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("C运行");
                    runWho = 1;
                    c1.signal();
                    lock.unlock();
                }
            }
        }.start();

    }
}
