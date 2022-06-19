package com.wyq.rpc.core.extension;

/**
 * @Author wyq
 * @Date 2022-06-19 11:17
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
