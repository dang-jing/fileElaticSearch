package com.example.fileElaticSearch.tuling.service.impl;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author：小党
 * @Date:Created in 20:08  2021/5/25
 * @Explain：
 */
public class Demo {

    //3.静态内部类
    static class Like2 implements ILike {
        @Override
        public void lambda() {
            System.out.println("i like lambda2");
        }
    }

    public static void main(String[] args) throws Exception {
        ILike like = new Like();
        like.lambda();

        ILike like2 = new Like2();
        like2.lambda();


        ReentrantLock lock = new ReentrantLock();
        try {
            lock.lock();//加锁
        }finally {
            //解锁
            lock.unlock();
        }



        //4.局部内部类
        class Like3 implements ILike {
            @Override
            public void lambda() {
                System.out.println("i like lambda3");
            }
        }
        ILike like3 = new Like3();
        like3.lambda();

        //5.匿名内部类,没有类的名称，必须借助接口或父类
        like = new ILike() {
            @Override
            public void lambda() {
                System.out.println("i like lambda4");
            }
        };
        like.lambda();


        //6.lambda简化
        like = () -> {
            System.out.println("i like lambda5");
        };
        like.lambda();

        //必须要有函数式接口ILove
        ILove love = null;
        love = (int a) -> {
            System.out.println("i love youo -->" + a);
        };
        //简化：
        love=(a)->{
            System.out.println("i love youo -->"+a);
        };
        love =a->{
            System.out.println("i love youo -->"+a);
        };
        //最终简化：
        love = a -> System.out.println("i love youo -->" + a);
        love.lambda(520);
    }

}

//1.函数式接口
interface ILike {
    void lambda();
}

//1.函数式接口
interface ILove {
    void lambda(int a);
}

//2.实现类
class Like implements ILike {
    @Override
    public void lambda() {
        System.out.println("i like lambda");
    }
}