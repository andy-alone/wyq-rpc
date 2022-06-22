package com.wyq.rpc.core.remoting.netty.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author wyq
 * @Date 2022-06-19 23:11
 */
@Slf4j
public class DefaultClass {

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                //close();
            }
        }));
    }

    private static void close() {
        try {
            System.out.println("clean");
            executorService.shutdown();
            System.out.println(executorService.awaitTermination(10000, TimeUnit.SECONDS));
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int index = i;
            try {
                executorService.schedule(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("yes" + index);

                    }
                }, 3, TimeUnit.SECONDS);
                System.out.println("add thread");
            } catch (Exception e) {
            }

        }

    }
}
