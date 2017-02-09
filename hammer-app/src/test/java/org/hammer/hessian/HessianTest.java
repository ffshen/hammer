package org.hammer.hessian;
   

import org.hammer.AppStarter;
import org.hammer.client.AppHessianClient;
import org.hammer.context.AppContext;
import org.hammer.domain.SampleDomain; 
import org.hammer.utils.JsonUtil; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
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
@SpringApplicationConfiguration(classes = AppStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//不加此注解，会报异常，NoSuchBeanDefinitionException，javax.servlet.ServletContext
public class HessianTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());   

    private static final Integer orderId = 76 ;
    
    static{
        AppContext.getTraceId() ;
        AppContext.setNeedClear(false);
    }
    

    
//    @BeforeClass
//    public static void init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method method = HessianClient.class.getDeclaredMethod("createServiceApi", String.class, HessianClientAPI.class.getClass());
//        method.setAccessible(true);
//        HessianClientAPI api = (HessianClientAPI)method.invoke(HessianClientAPI.class, HessianClientConstants.SERVICE_PATH + HessianClientConstants.SERVICE_API, HessianClientAPI.class);
//        Deencapsulation.setField(HessianClient.class, "api", api);
//    }
    
    @Test
    public void testHessian(){
        logger.info("HessianTest testHessian traceid : {}", AppContext.getTraceId() );        
        SampleDomain domain = AppHessianClient.getHessianApi().selectByPk(orderId) ;
        logger.info("HessianTest testHessian : {}",JsonUtil.toJson(domain));
    }
}
