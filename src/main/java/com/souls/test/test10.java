package com.souls.test;
/**
 * 异步模式之生产者/消费者
 * 要点
 * 1.与前面的保护性暂停中的 GuardObject 不同，不需要产生结果和消费结果的线程一一对应
 * 2.消费队列可以用来平衡生产和消费的线程资源
 * 3.生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据
 * 4.消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据
 * 5.JDK 中各种阻塞队列，采用的就是这种模式
 */

import lombok.extern.slf4j.Slf4j;
import java.util.LinkedList;

@Slf4j(topic = "c.test10")
public class test10 {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            },"生产者" + i).start();
        }


        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = queue.take();
            }
        },"消费者").start();
    }
}

//消息队列类， java线程之间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue {
    //消息队列的集合
    private LinkedList<Message> list = new LinkedList<>();

    //队列的容量
    private int capcity;

    MessageQueue(int capcity) {
        this.capcity = capcity;
    }

    //获取消息
    public Message take() {
        //检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //从队列头部取元素并返回
            Message message = list.removeFirst();
            log.debug("已消费消息 {}",message);
            list.notifyAll();
            return message;
        }
    }

    //存入消息
    public void put(Message message) {
        synchronized (list) {
            //检查队列是否已满
            while (list.size() == capcity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //在队列尾部添加元素
            list.addLast(message);
            log.debug("已生产消息 {}",message);
            list.notifyAll();
        }
    }
}

final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
