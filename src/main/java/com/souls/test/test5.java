package com.souls.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test5")
public class test5 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination t = new TwoPhaseTermination();
        t.start();

        Thread.sleep(3500);
        t.stop();
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination{
    private Thread th;

    public void start(){
        th = new Thread(() -> {
            while (true){
                Thread cur = Thread.currentThread();
                if(cur.isInterrupted()){
                    log.debug("料理后事。。。");
                    break;
                }
                try{
                    Thread.sleep(1000);
                    log.debug("执行监控记录。。。");
                }catch (Exception e){
                    e.printStackTrace();
                    //重新设置打断标记
                    cur.interrupt();
                }
            }
        });

        th.start();
    }

    public void stop(){
        th.interrupt();
    }

}


