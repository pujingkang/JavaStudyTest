package com.souls;

import com.souls.pojo.Season;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaStudyTestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1(){
        int a = 1;
        int b = 2;
        assert a < b : "a小于b"; //断言为true，继续执行；否则输出断言内容
        System.out.println(a + b);
    }

    @Test
    public void test2(){
        System.out.println(Season.SPRING);
        System.out.println(Season.SPRING.getSeasonDesc());
        System.out.println(Season.SUMMER);
        System.out.println(Season.SUMMER.getSeasonDesc());
        System.out.println(Season.AUTUMN);
        System.out.println(Season.AUTUMN.getSeasonDesc());
        System.out.println(Season.WINTER);
        System.out.println(Season.WINTER.getSeasonDesc());
    }

    @Test
    public void test3(){
        System.out.println("hello");
    }
}
