package org.hammer.concurrency.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors; 
 

/**
 * @author shenx
 * @date 2017年1月18日
 * @see
 * @version
 */
public class ExecutorServiceFactory    {

    private static final Integer threadSize = 10 ;    
    
    private static class LazyHolder{        
        private static final  ExecutorService pool  ;
        static {
            pool = Executors.newFixedThreadPool(threadSize) ;
        }   
    } 
    
    public static ExecutorService getExecutorService(){
        return LazyHolder.pool ;
    }
}
