package org.hammer.cache;

import java.util.List;
import java.util.Set;

/**
 * 统一对外的缓存接口
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public interface ICache {
    /**
     * 从缓存中取出一个数据对象
     * @param key {String} -- cache key
     * @return 返回Object 或者 NULL
     * @throws CacheException 缓存异常
     */
    Object get(String key, Class<?> clazz) throws CacheException;
    
    /**
     * 添加一个数据对象到缓存
     * @param key key {String} -- cache key
     * @param value value
     * @throws CacheException 缓存异常
     */
    void set(String key, Object value) throws CacheException;

    /**
     * 从缓存中销毁/删除Key对应的数据
     * @param key key {String} -- cache key
     * @throws CacheException 缓存异常
     */
    void evict(String key) throws CacheException;

    /**
     * 从缓存中批量销毁/删除Keys对应的数据
     * @param keys {List} -- cache keys
     * @throws CacheException 缓存异常
     */
    void evict(List<?> keys) throws CacheException;

    /**
     * 取得当前region下所有的key
     * @return keys {List}
     * @throws CacheException 缓存异常
     */
    Set<String> keys(String pattern) throws CacheException;

    /**
     * 清除所有的缓存数据
     * @throws CacheException 缓存异常
     */
    void clear(String key) throws CacheException;

    /**
     * 销毁连接实例 -- 实际做的工作是清理掉所有数据
     * @throws CacheException 缓存异常
     */
    void destroy() throws CacheException;
    /**
     * 自增key对应的value
     * @param key
     * @return
     * @throws CacheException
     */
    Long incr(String key) throws CacheException;
    /**
     * 自增key对应的value
     * @param key
     * @return
     * @throws CacheException
     */
    Long incr(String key, Integer times) throws CacheException;
    
    /**
     * 当前缓存是否可用
     * @return
     */
    boolean isAvailable();

    /**
     *添加一个数据对象到缓存，并设置过期时间
     * @param key
     * @param seconds 过期时间，单位（秒）
     * @param value
     * @throws CacheException
     */
    void setex(String key, int seconds, Object value) throws CacheException;
}
