package org.hammer.concurrency.function;

import java.util.function.Function;

import org.hammer.context.AppContext;
 

/**
 * Created by ansion on 16/10/26.
 */
public class FunctionWithContext<T, R> implements Function<T, R> {
	AppContext parentContext; //save parent's context
    Function<T, R> function;
    public FunctionWithContext(Function<T, R> function) {
        parentContext = AppContext.current();
        this.function = function;
    }
    @Override
    public R apply(T t) {
        //back this thread's context
    	AppContext orig = AppContext.current();
        //clone parent context
    	AppContext.current(parentContext);
    	AppContext newContext = AppContext.copyFromCurrent();
    	AppContext.current(newContext);
        R result;
        try {
            result = function.apply(t);
        } catch (Exception e) {
            throw e;
        } finally {
            //recover context
        	AppContext.current(orig);
        }
        return result;
    }
}
