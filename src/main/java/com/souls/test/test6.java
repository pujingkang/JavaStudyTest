package com.souls.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test6")
public class test6 {

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room){
                    room.increase();
                }
            }

        },"t1");

        Thread t2 = new Thread(() -> {

            for (int i = 0; i < 5000; i++) {
                synchronized (room){
                    room.decrease();
                }
            }

        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("count : " + room.getCount());
    }
}

class Room{
    private int count;

    public synchronized void increase(){
        count++;
    }

    public synchronized void decrease(){
        count--;
    }

    public synchronized int getCount(){
        return this.count;
    }
}
