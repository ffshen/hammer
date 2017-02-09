package org.hammer.hessian;
  
  
import org.hammer.client.AppHessianClient;
import org.hammer.context.AppContext;
import org.hammer.domain.SampleDomain; 
import org.hammer.utils.JsonUtil;
import org.junit.Test; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
 
 
 

/**
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */

//@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
//@SpringApplicationConfiguration(classes = AppStarter.class) // 指定我们SpringBoot工程的Application启动类
//@WebIntegrationTest
public class HessianTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());   

    private static final Integer orderId = 76 ;
    
    static{
        AppContext.getTraceId() ;
        AppContext.setNeedClear(false);
    }
    
   
    @Test
    public void testHessian(){
        logger.info("HessianTest testHessian traceid : {}", AppContext.getTraceId() );        
        SampleDomain domain = AppHessianClient.getHessianApi().selectByPk(orderId) ;
        logger.info("HessianTest testHessian : {}",JsonUtil.toJson(domain));
    }
}
