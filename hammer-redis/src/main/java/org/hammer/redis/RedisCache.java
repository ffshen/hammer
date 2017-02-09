package org.hammer.redis;

import java.util.List;
import java.util.Set;

import org.hammer.cache.CacheException;
import org.hammer.cache.ICache;
import org.hammer.cache.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 

import redis.clients.jedis.JedisCluster;
 
 

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
@Service
public class RedisCache implements ICache{
 

    private Serializer serializer = new JSONSerializer();
    
    @Autowired
    private JedisCluster cluster  ;

    @Override
    public Object get(String key, Class<?> clazz) throws CacheException {
        if (isAvailable()) {
            return serializer.deSerialize(cluster.get(key), clazz);
        }
        
        return null;
    }

    @Override
    public void set(String key, Object value) throws CacheException {
        if (isAvailable()) {
            cluster.set(key, serializer.serialize(value));
        } else {
            throw new CacheException("Redis缓存当前不可用");
        }

    }
    @Override
    public void setex(String key, int seconds, Object value) throws CacheException {
        if (isAvailable()) { 
            cluster.setex(key, seconds, serializer.serialize(value));
        } else {
            throw new CacheException("Redis缓存当前不可用");
        }
    }
    @Override
    public void evict(String key) throws CacheException {
    }

    @Override
    public void evict(List<?> keys) throws CacheException {

    }

    @Override
    public Set<String> keys(String pattern) throws CacheException {
        return cluster.hkeys(pattern);
    }

    @Override
    public void clear(String key) throws CacheException {
        cluster.del(key);
    }
    @Override
    public Long incr(String key) throws CacheException {
        return cluster.incr(key);
    }
    @Override
    public Long incr(String key, Integer times) throws CacheException {
        return cluster.incrBy(key, times);
    }
    @Override
    public void destroy() throws CacheException {

    }

    @Override
    public boolean isAvailable() {
        return cluster != null;
    }

}
