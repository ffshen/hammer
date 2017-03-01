package org.hammer.dubbo;
  
  
import org.hammer.TestStarter; 
import org.hammer.context.AppContext; 
import org.hammer.service.DubboService; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; 
 
 
 

/**
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = TestStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest
public class DubboTest {

    Logger logger = LoggerFactory.getLogger(this.getClass()); 
    
    @Autowired
    DubboService service ; 
    
    static{
        AppContext.getTraceId() ;
        AppContext.setNeedClear(false);
    }
    
   
    @Test
    public void testDubbo() throws InterruptedException{   
    	int i = 25 ;
    	while(i > 0){
    		service.say("world");
    		i--;
    		AppContext.clear();
    		AppContext.getTraceId() ;
    	}
    	Thread.sleep(10000);
    }
}
