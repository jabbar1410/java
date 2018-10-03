package com.itheima.other;

/**
 * 单例设计模式：
 *      在整个应用中，某个类的实例对象只有一个
 *
 *      饿汉式
 *
 *      懒汉式
 */
public class SingleInstanceTest {
    public static void main(String[] args) {
        SingleInstance2 instance = SingleInstance2.getInstance();
        SingleInstance2 instance1 = SingleInstance2.getInstance();
        System.out.println(instance == instance1);
    }
}

/**
 * 饿汉式：类加载时，直接实例化，没有线程安全问题
 */
class SingleInstance1{

    private static SingleInstance1 instance1 = new SingleInstance1();

    private SingleInstance1(){}

    public static SingleInstance1 getInstance(){
        return instance1;
    }
}

/**
 * 懒汉式单例设计模式的标准写法（双重校验）
 *  类加载时，不实例化对象，当调用getInstance时，再实例化，为了保证多线程的安全问题，需要实现同步锁并实现双重校验
 */
class SingleInstance2{
    private static SingleInstance2 instance = null;
    private SingleInstance2(){}

    public static SingleInstance2 getInstance(){
        if (instance == null) {
            synchronized (SingleInstance2.class) {
                if (instance == null) {
                    instance = new SingleInstance2();
                }
            }
        }
        return instance;
    }
}
