package com.souls.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test4")
public class test4 {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1"){
            @Override
            public void run() {
                log.debug("running...");
            }
        };
        System.out.println(t1.getState());
        t1.start();
        System.out.println(t1.getState());
    }
}
