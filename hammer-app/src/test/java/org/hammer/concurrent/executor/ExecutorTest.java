package org.hammer.concurrent.executor;
 
import java.util.Arrays;
import java.util.List;
import java.util.Random; 
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.hammer.AppStarter;
import org.hammer.mybatis.properties.MybatisProperties;
import org.hammer.utils.JsonUtil;
import org.hammer.vo.TestResultVO;
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
 * @date 2017年1月14日
 * @see
 * @version
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@SpringApplicationConfiguration(classes = AppStarter.class) // 指定我们SpringBoot工程的Application启动类
@WebIntegrationTest//如果不标识,那么@EnableRedisHttpSession会因为无法注入javax.servlet.servletcontext而报错
public class ExecutorTest {

    private static final Logger log = LoggerFactory.getLogger(ExecutorTest.class);    

    private static final  List<String> defaultThreadName = Arrays.asList(new String[]{"广州线程","佛山线程","东莞线程" });
    
    @Autowired
    private MybatisProperties myBatisProp ;
    
    @Test
    public void testYml(){
        log.info("myBatisProp configLocation : {}",myBatisProp.getConfigLocation());
        
    }
     
    @Test
    public void callbackAndFutrueTest() throws InterruptedException, ExecutionException{
        ExecutorService threadPool = Executors.newFixedThreadPool(2) ;
        
        CompletionService<TestResultVO> cs = new ExecutorCompletionService<TestResultVO>(threadPool);
        
        List< TestCallable<TestResultVO> > callables = defaultThreadName.stream().map( name->{            
            TestCallable<TestResultVO> testCallable = new TestCallable<TestResultVO> (name){
                public TestResultVO call() throws Exception {                    
                    log.info("threadName:{}",getThreadName());
                    return new TestResultVO(new Random().nextInt(100) ,getThreadName() ) ;                    
                }
            } ;
            return testCallable ;
        }).collect(Collectors.toList()) ;    
        
        callables.stream().forEach((callable)->  cs.submit(callable) );
         
        
        //take阻塞
        for(int i = 0 ; i < defaultThreadName.size() ; i++){
            TestResultVO result = cs.take().get() ;
            log.info("运行结果：{}",JsonUtil.toJson(result ));
        }        
        //poll 不阻塞
        //cs.poll().get() ;
        
        threadPool.shutdown(); 
    }
}
