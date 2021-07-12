package com.example.fileElaticSearch.tuling.service.impl;

/**
 * @Author：小党
 * @Date:Created in 16:50  2021/5/26
 * @Explain：
 */
public class TestJoin implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {
            System.out.println("线程vip来了-->"+i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //启动线程
        TestJoin testJoin = new TestJoin();
        Thread thread = new Thread(testJoin);
        thread.start();

        for (int i = 0; i < 500; i++) {
            //强制执行
            if (i==200){
                thread.join();
            }
            System.out.println("main-->"+i);
        }
    }
}
