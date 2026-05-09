package com.lcy;

public class ThreadLocalTest {

    public static ThreadLocal<String> local = new ThreadLocal<>();
    public static void main(String[] args) {

        local.set("Main Message");

        System.out.println(Thread.currentThread().getName() + ":" + local.get() );

        new Thread(new Runnable() {
            @Override
            public void run() {
                local.set("Thread Message");
                System.out.println(Thread.currentThread().getName() + ":" + local.get());
            }
        }).start();

        local.remove();

        System.out.println(Thread.currentThread().getName() + ":" + local.get());

    }
}
