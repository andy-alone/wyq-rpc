package com.wyq.rpc.core.util;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author wyq
 * @Date 2022-06-23 0:49
 */
public class CustomThreadFactory implements ThreadFactory {

    final AtomicInteger index = new AtomicInteger(1);

    public CustomThreadFactory() {
        System.out.println(1);
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "service-handle-group" + index.getAndIncrement());
    }
}
