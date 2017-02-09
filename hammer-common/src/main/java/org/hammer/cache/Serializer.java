package org.hammer.cache;

/**
 * 缓存序列化接口，允许多种实现<br>
 * 目前两种序列化方式：<br>
 * 1.StringSerializer<br>
 * 2.FstSerializer搭载在Snappy上面实现<br>
 * 3.JdkSerializer
 * 4.
 */
public interface Serializer {
    
    String serialize(final Object value);
    
    Object deSerialize(String value, Class<?> clazz);
}
