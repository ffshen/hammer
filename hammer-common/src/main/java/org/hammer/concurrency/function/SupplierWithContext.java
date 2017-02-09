package org.hammer.concurrency.function;
 

import java.util.function.Supplier;

import org.hammer.context.AppContext;

/**
 * Created by ansion on 16/10/20.
 */
public class  SupplierWithContext<T> implements Supplier<T> {
	AppContext parentContext; //save parent's context
    Supplier<T> supplier;
    public SupplierWithContext(Supplier<T> supplier) {
        parentContext = AppContext.current();
        this.supplier = supplier;
    }

    @Override
    public T get() {
        //back this thread's context
    	AppContext orig = AppContext.current();
        //clone parent context
    	AppContext.current(parentContext);
    	AppContext newContext = AppContext.copyFromCurrent();
    	AppContext.current(newContext);
        T result;
        try {
            result = supplier.get();
        } catch (Exception e) {
            throw  e;
        } finally {
            //recover context
        	AppContext.current(orig);
        }
        return result;
    }
}
