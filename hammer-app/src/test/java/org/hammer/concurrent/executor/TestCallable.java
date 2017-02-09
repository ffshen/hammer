package org.hammer.concurrent.executor;

import java.util.concurrent.Callable; 
/**
 * @author shenx
 * @date 2017年1月14日
 * @see
 * @version
 * @param <T>
 */
public abstract class TestCallable<T> implements Callable<T>{   
 
    
    private String threadName ; 
    
    public TestCallable(String threadName){
        this.threadName = threadName ;
    }
    
    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
 

}
