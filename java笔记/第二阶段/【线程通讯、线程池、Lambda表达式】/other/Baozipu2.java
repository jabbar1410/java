package com.itheima.other;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Baozipu2 {
    private int maxNum = 10; //包子铺能够生产的最大数量
    private int count = 8; // 当前包子铺生产的包子数量
    private Lock lock = new ReentrantLock();

    private Condition productCondition = lock.newCondition();//生产者的状态对象
    private Condition consumerCondition = lock.newCondition(); //消费者的状态对象

    public  void productBaozi(){
        lock.lock();
        if (count<maxNum){
            count++; //生产了包子
            System.out.println(Thread.currentThread().getName() + "生产了一个包子，当前包子数为：" + count);
            consumerCondition.signalAll(); //通知消费者消费包子
        }else{
            try {
                System.out.println("包子太多，停止生产");
                productCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.unlock();
    }

    public  void sellBaozi(){
        lock.lock();
        if(count > 0){
            count--;
            System.out.println(Thread.currentThread().getName() + "消费了一个包子，当前剩余包子数为：" +count);
            productCondition.signalAll(); // 通知生产者生产包子
        }else{
            try {
                System.out.println("包子卖完了，停止消费");
               consumerCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.unlock();
    }

}
