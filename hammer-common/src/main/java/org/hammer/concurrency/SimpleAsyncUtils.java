package org.hammer.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

import org.hammer.concurrency.function.SupplierWithContext;
import org.hammer.concurrency.pool.ExecutorServiceFactory;

/**
 * Created by ansion on 16/10/20.
 */
public class SimpleAsyncUtils {
    /**
     * Returns a new CompletableFuture that is asynchronously completed
     * by a task running in the {@link ForkJoinPool#commonPool()} with
     * the value obtained by calling the given Supplier.
     *
     * @param supplier a function returning the value to be used
     * to complete the returned CompletableFuture
     * @param <U> the function's return type
     * @return the new CompletableFuture
     */
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(new SupplierWithContext<U>(supplier));
    }
    
    public static <U> CompletableFuture<U> supplyAsyncWithPool(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(new SupplierWithContext<U>(supplier),ExecutorServiceFactory.getExecutorService());
    }
}
