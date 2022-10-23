package com.souls.test;
/**
 * 同步模式之保护性暂停
 * 即 Guarded Suspension，用在一个线程等待另一个线程的执行结果
 * 要点:
 * 1.有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
 * 2.如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 * 3.JDK 中，join 的实现、Future 的实现，采用的就是此模式
 * 4.因为要等待另一方的结果，因此归类到同步模式
 */

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.test9")
public class test9 {
    //模拟线程一等待线程二的下载结果
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        Thread.sleep(1000);

        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容" + id).start();
        }
    }
}

@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        //收信
        GuardedObject guardedObject = MailBoxes.createGuardedObject();
        log.debug("开始收信 id:{}",guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容:{}",guardedObject.getId(), mail);
    }

}

@Slf4j(topic = "c.Postman")
class Postman extends Thread{
    private int id;
    private String mail;

    public Postman (int id, String mail){
        this.id = id;
        this.mail = mail;
    }
    @Override
    public void run() {
        GuardedObject guardedObject = MailBoxes.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}",id, mail);
        guardedObject.complete(mail);
    }
}

class MailBoxes{
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();
    private static int id = 1;

    //产生唯一id
    private static synchronized int generateId(){
        return id++;
    }

    public static GuardedObject getGuardedObject(int id){
        return boxes.remove(id);
    }

    public static GuardedObject createGuardedObject(){
        GuardedObject go = new GuardedObject(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
}

class GuardedObject{
    // 标识 Guarded Object
    private int id;
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //获取结果
    //timeout代表要等待多久
    public Object get(long timeout){
        synchronized (this){
            //开始时间
            long begin = System.currentTimeMillis();
            //经历的时间
            long passedTime = 0;
            //没有结果时，持续等待
            while (response == null){
                //本轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                //超过最大等待时间时，退出
                if(waitTime <= 0){
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                //得到经历的时间
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response){
        synchronized (this){
            this.response = response;
            this.notifyAll();
        }
    }
}

