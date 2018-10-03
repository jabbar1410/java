package com.itheima.other;

/**
 * sleep 和 wait 计时等待的区别：
 *  sleep不会释放锁资源
 *  wait会释放锁资源
 */
public class SleepAndWait {
    public static void main(String[] args) {

        Object obj = new Object();

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (obj) {
                    System.out.println("=====");
                    try {
                        obj.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("线程1 执行了");
                }
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        new Thread(){
            @Override
            public void run() {
                synchronized (obj){
                    System.out.println("线程2 执行了");
                }
            }
        }.start();

    }
}
