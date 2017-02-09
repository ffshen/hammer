package org.hammer.redis.config;

import org.hammer.redis.properties.RedisSentinelProperties;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import; 
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
@Configuration
@Import(RedisSentinelProperties.class)
public class RedisTemplateConfig {
    
    @Autowired
    private RedisSentinelProperties sentinelProperties ;
    
    @Bean(name = "MyRedisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        RedisConnectionFactory factory = redisSentinelConnectionFactory();
        StringRedisTemplate template = new StringRedisTemplate(factory);
        return template;
    }
    
//    @Bean
    public JedisConnectionFactory redisSentinelConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory(redisSentinelConfiguration(), jedisPoolConfig());
        //这句不用设置       factory.setHostName("172.16.1.153");
        factory.setPassword(sentinelProperties.getPassword());
        factory.setPort(sentinelProperties.getPort());
        factory.setDatabase(sentinelProperties.getDatabase());
        factory.setUsePool(true);
        //这句相当重要，否则会报
        //RedisConnectionFailureException: Cannot get Jedis connection;
        factory.afterPropertiesSet();
        return factory;
    }
    
    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        RedisSentinelConfiguration poolCfg = new RedisSentinelConfiguration()
                .master(sentinelProperties.getMaster());
        if (StringUtils.isEmpty(sentinelProperties.getNodes())) {
            return null;
        }
        String[] nodes = sentinelProperties.getNodes().split(",");
        for (String node : nodes) {
            String[] nodeInfo = node.split(":");
            poolCfg.sentinel(nodeInfo[0], Integer.valueOf(nodeInfo[1]));
        }
        return poolCfg;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }
}
