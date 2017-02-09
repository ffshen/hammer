package org.hammer.concurrency.function;


import java.util.function.Consumer;

import org.hammer.context.AppContext;

/**
 * Created by ansion on 16/10/20.
 */
public class ConsumerWithContext<T> implements Consumer<T> {
	
    AppContext parentContext; //save parent's context
    
    Consumer<T> consumer;
    
    public ConsumerWithContext(Consumer<T> consumer) {
        parentContext = AppContext.current();
        this.consumer = consumer;
    }
    
    @Override
    public void accept(T t) {
        //back this thread's context
    	AppContext orig = AppContext.current();
        //clone parent context
    	AppContext.current(parentContext);
    	AppContext newContext = AppContext.copyFromCurrent();
    	AppContext.current(newContext);
        try {
            consumer.accept(t);
        } catch (Exception e) {
            throw  e;
        } finally {
            //recover context
        	AppContext.current(orig);
        }
    }
}
