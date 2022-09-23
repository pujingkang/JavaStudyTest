package com.souls.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test1")
public class Test1 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> log.debug("running..."));
        t.setName("t1");
        t.start();

        log.debug("running...");
    }
}
