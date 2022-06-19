package com.wyq.rpc.core.serialization;

/**
 * 序列化接口
 * @Author wyq
 * @Date 2022-06-19 11:40
 */
public interface Serialization {

    /**
     * 序列化
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
