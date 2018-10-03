package com.itheima.other;

public class Producter extends Thread {
    private Baozipu2 baozipu;
    public Producter(Baozipu2 baozipu,String name){
        super(name);
        this.baozipu = baozipu;
    }
    @Override
    public void run() {
        while (true){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            baozipu.productBaozi();
        }
    }
}

