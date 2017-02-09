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
         prefix = RedisSentinelProperties.REDIS_SENTINEL_PREFIX)
public class RedisSentinelProperties {


    public  static final String REDIS_SENTINEL_PREFIX = "spring.redis.sentinel" ;  
    
    private Boolean useSentinel ;
    
    private String master ;
    
    private String nodes ;
    
    private String password ;
    
    private int port ;
    
    private int database ;
    
    

    public Boolean getUseSentinel() {
        return useSentinel;
    }

    public void setUseSentinel(Boolean useSentinel) {
        this.useSentinel = useSentinel;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
    
    
    
}
