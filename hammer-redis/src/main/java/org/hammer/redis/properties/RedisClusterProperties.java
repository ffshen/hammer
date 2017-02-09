package org.hammer.redis.properties;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
@Configuration
@ConfigurationProperties(
        locations = "classpath:redis.yml",
        prefix = RedisClusterProperties.REDIS_CLUSTER_PREFIX)
public class RedisClusterProperties {

    public  static final String REDIS_CLUSTER_PREFIX = "spring.redis.cluster" ;      

    private String hosts ;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
    
    
}
