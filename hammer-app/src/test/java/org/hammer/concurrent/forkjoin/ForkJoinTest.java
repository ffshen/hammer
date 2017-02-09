package org.hammer.concurrent.forkjoin;
 
import java.util.Arrays; 
import java.util.List; 
import java.util.concurrent.ForkJoinPool; 
import java.util.concurrent.Future;

import org.hammer.utils.JsonUtil;
import org.hammer.vo.TestResultVO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenx
 * @date 2017年1月15日
 * @see
 * @version
 */
public class ForkJoinTest {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinTest.class);    

    private static final  List<String> defaultThreadName = Arrays.asList(new String[]{"广州线程","佛山线程","东莞线程","清远线程","肇庆线程" });
    
    @Test
    public void forkJoinTest() throws Exception {
        ForkJoinPool pool=new ForkJoinPool(3);
        
        Future<List<TestResultVO>> future = pool.submit(new TestRecursiveTask (defaultThreadName)) ;
        
        log.info("forkJoinTest result:{}",JsonUtil.toJson(future.get())) ;
        
        pool.shutdown();
    }
    
}
