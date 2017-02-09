package org.hammer.concurrency.pool;

import java.util.concurrent.ForkJoinPool;
 

/**
 * @author shenx
 * @date 2017年1月18日
 * @see
 * @version
 */
public class ForkJoinPoolFactory extends ForkJoinPool {

   private static final Integer threadSize = 10 ;
    
    public ForkJoinPoolFactory(Integer threadSize){
        super(threadSize) ;
    }
    
    private static class LazyHolder{        
        private static final  ForkJoinPoolFactory pool  ;
        static {
            pool = new ForkJoinPoolFactory(threadSize) ;
        }   
    } 
    
    public static ForkJoinPoolFactory getForkJoinPool(){
        return LazyHolder.pool ;
    }
    
}
