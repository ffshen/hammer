package org.hammer.cache;

 
import org.hammer.AppStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest; 
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = AppStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest
public class CacheTest {

    private static final Logger log = LoggerFactory.getLogger(CacheTest.class); 

    @Autowired
    @Qualifier("MyRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private ICache cache ;
    
    @Test
    public void testRedisTemplate() throws Exception {
        redisTemplate.opsForValue().set("5:401180146", "101");
        log.info("CacheService testRedisTemplate , {} ",redisTemplate.opsForValue().get("5:401180146")) ;        
       
    }
    
    @Test
    public void testICache() throws CacheException{
        cache.set("testKey", "testValue");
        log.info("testICache : {}",cache.get("testKey",String.class));
    }
}
