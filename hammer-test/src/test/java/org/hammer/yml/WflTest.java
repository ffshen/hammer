package org.hammer.yml;

import org.hammer.TestStarter;
import org.hammer.utils.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = TestStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//不加此注解，会报异常，NoSuchBeanDefinitionException，javax.servlet.ServletContext
@ComponentScan("org.hammer")
public class WflTest {
	
	@Autowired
	private WflProperties properties ;

    Logger logger = LoggerFactory.getLogger(this.getClass());   
    
    @Test
    public void ymlTest(){
    	logger.info("order:"+JsonUtil.toJson(properties.getWflOrder()));
//    	logger.info("control:"+JsonUtil.toJson(properties.getWflControls()));
    }
}
