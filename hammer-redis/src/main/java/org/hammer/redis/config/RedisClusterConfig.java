package org.hammer.redis.config;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hammer.cache.CacheException;
import org.hammer.redis.properties.RedisClusterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */

@Configuration
@EnableCaching
@Import(RedisClusterProperties.class)
public class RedisClusterConfig extends CachingConfigurerSupport {
    private static final Logger logger = LoggerFactory.getLogger(RedisClusterConfig.class); 
    
    @Autowired
    private RedisClusterProperties clusterProperties ;

    @Bean
    public JedisCluster getJedisCluster() throws CacheException {
        String hosts = clusterProperties.getHosts()  ;
        if (isBlank(hosts)) {
            throw new CacheException("spring.redis.hosts={ip1:port1},{ip2:port2},{ip3:port3},...");
        }
        logger.debug("Initializing Redis cluster with '{}' ...", hosts);
        try {
            Set<HostAndPort> nodes = Stream.of(hosts.split(","))
                    .map(host -> {
                        String[] hostAndPort = host.split(":");
                        return new HostAndPort(hostAndPort[0], toInt(hostAndPort[1]));
                    }).collect(Collectors.toSet());
            logger.info("Redis cluster connected successfully! hosts={}", hosts);
            return  new JedisCluster(nodes);
        } catch (Exception e) {
            throw new CacheException("Please check if 'spring.redis.hosts' was configured properly!", e);
        }
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String hosts = clusterProperties.getHosts()  ;
        if (isBlank(hosts)) {
            throw new RuntimeException("spring.redis.hosts={ip1:port1},{ip2:port2},{ip3:port3},...");
        }
        logger.debug("Initializing Spring Redis Data cluster with '{}' ...", hosts);
        try {
            RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(
                    new RedisClusterConfiguration(Stream.of(hosts.split(",")).collect(Collectors.toList())));
            logger.info("Spring Redis Data cluster connected successfully! hosts={}", hosts);
            return redisConnectionFactory ;
        } catch (Exception e) {
            throw new RuntimeException("Please check if 'spring.redis.hosts' was configured properly!", e);
        } 

    } 
}
