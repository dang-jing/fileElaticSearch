package com.example.fileElaticSearch.tuling.service.impl;



/**
 * @Author：小党
 * @Date:Created in 10:33  2021/5/26
 * @Explain：
 */
public class 实现类 implements Runnable {
    private static String 胜利者;


    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            //模拟兔子休息
            if (Thread.currentThread().getName().equals("兔子")) {
                try {
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            //判断比赛是否结束
            boolean flag = 跑道(i);
            //比赛结束，停止程序
            if (flag) {
                break;
            }
            System.out.println(Thread.currentThread().getName() + "-->跑了" + i + "步");
        }
    }

    //判断是否完成比赛
    private boolean 跑道(int 步数) {
        //判断是否有胜利者
        if (胜利者 != null) {
            return true;
        }
        {
            if (步数 >= 100) {
                //获取该线程的名字
                胜利者 = Thread.currentThread().getName();
                System.out.println("胜利者 是" + 胜利者);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        实现类 实现类 = new 实现类();
        //创建线程对象并设置线程名
        new Thread(实现类, "兔子").start();
        new Thread(实现类, "乌龟").start();
    }
}
