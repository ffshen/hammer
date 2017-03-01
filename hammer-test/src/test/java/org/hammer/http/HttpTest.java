package org.hammer.http;
 
import org.hammer.TestStarter;
import org.hammer.context.AppContext; 
import org.hammer.mvc.controller.DefaultWebApiResult;
import org.hammer.mvc.http.SimpleHttpClient; 
import org.hammer.utils.JsonUtil; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
import com.google.common.util.concurrent.FutureCallback;
 

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = TestStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//不加此注解，会报异常，NoSuchBeanDefinitionException，javax.servlet.ServletContext
@ComponentScan("org.hammer")
public class HttpTest {
	 

    Logger logger = LoggerFactory.getLogger(this.getClass());   

    private static final Integer orderId = 76 ;
    
    static{
        AppContext.getTraceId() ;
        AppContext.setNeedClear(false);
    }
     
    
    @Test
    public void testHttp() {    	
    	SampleVo s = new SampleVo() ;
    	s.setOrderId(orderId);     	 
    	DefaultWebApiResult resp = (DefaultWebApiResult) SimpleHttpClient
    		.prepareDefaultWebApiResult()
    		.withUrl("http://localhost:10010/sample/test")
    		.withBody(s)
    		.post()
    		.getBody();
    	
    	logger.info("testHttp : " + JsonUtil.toJson(resp) );
    }
    
    @Test
    public void testAsyncHttp() throws InterruptedException {    	
    	
    	SampleVo s = new SampleVo() ;
    	s.setOrderId(orderId);     	 
    	
    	SimpleHttpClient
    		.prepare(SampleVo.class)
    		.withUrl("http://localhost:9000/test/2/testpath")
    		.withBody(s)
    		.withCallback(new FutureCallback<ResponseEntity<SampleVo>>(){
				@Override
				public void onSuccess(ResponseEntity<SampleVo> result) {
			    	logger.info("testAsyncHttp : " + JsonUtil.toJson(result.getBody()) );				
				}
				@Override	
				public void onFailure(Throwable t) {
					
				}	        	
	        })
    		.postAsync()  
    	
    	;

    	logger.info("testAsyncHttp sleep " );	
    	
    	Thread.sleep(10000);
    	logger.info("testAsyncHttp sleep end " );
    }
     
    



    
}
