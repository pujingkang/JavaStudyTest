package com.souls.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.test7")
public class test7 {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        new Thread(() -> {
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (dog){
                log.debug(ClassLayout.parseInstance(dog).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (test7.class){
                test7.class.notify();
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (test7.class){
                try {
                    test7.class.wait();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
            synchronized (dog){
                log.debug(ClassLayout.parseInstance(dog).toPrintable());
            }
            log.debug(ClassLayout.parseInstance(dog).toPrintable());
        },"t2").start();
    }
}

class Dog{

}

