package com.itheima.other;

public class Baozipu {
    private int maxNum = 10; //包子铺能够生产的最大数量
    private int count = 8; // 当前包子铺生产的包子数量

    public synchronized void productBaozi(){
        if (count<maxNum){
            count++; //生产了包子
            System.out.println(Thread.currentThread().getName() + "生产了一个包子，当前包子数为：" + count);
            notifyAll(); //通知消费者消费包子
        }else{
            try {
                System.out.println("包子太多，停止生产");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sellBaozi(){
        if(count > 0){
            count--;
            System.out.println(Thread.currentThread().getName() + "消费了一个包子，当前剩余包子数为：" +count);
            notifyAll(); // 通知生产者生产包子
        }else{
            try {
                System.out.println("包子卖完了，停止消费");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
