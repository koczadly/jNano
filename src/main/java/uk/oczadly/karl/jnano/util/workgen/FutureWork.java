/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Represents a {@code Future<GeneratedWork>} object which has yet to complete. Allows retrieval of root hash, and
 * provides a convenience cancel() method with no arguments.
 */
public class FutureWork implements Future<GeneratedWork> {
    
    private final Future<GeneratedWork> future;
    
    /**
     * @param future the future proxy object
     */
    public FutureWork(Future<GeneratedWork> future) {
        this.future = future;
    }
    
    
    /**
     * Attempts to cancel the work generation attempt, interrupting and stopping the generation task.
     *
     * <p>After this method returns, subsequent calls to {@link #isDone} will always return {@code true}. Subsequent
     * calls to {@link #isCancelled} will always return {@code true} if this method returned {@code true}.
     *
     * @return {@code true} if the work generation was cancelled
     */
    public boolean cancel() {
        return future.cancel(true);
    }
    
    /**
     * Attempts to cancel the work generation attempt, interrupting and stopping the generation task.
     *
     * <p>After this method returns, subsequent calls to {@link #isDone} will always return {@code true}. Subsequent
     * calls to {@link #isCancelled} will always return {@code true} if this method returned {@code true}.
     *
     * @param mayInterruptIfRunning value is ignored and always assumed {@code true}
     * @return {@code true} if the work generation was cancelled
     * @deprecated Use of parameterless {@link #cancel()} method is preferred.
     * @see #cancel()
     */
    @Override
    @Deprecated
    public final boolean cancel(boolean mayInterruptIfRunning) {
        return cancel();
    }
    
    @Override
    public final boolean isCancelled() {
        return future.isCancelled();
    }
    
    @Override
    public final boolean isDone() {
        return future.isDone();
    }
    
    @Override
    public final GeneratedWork get() throws InterruptedException, ExecutionException {
        return future.get();
    }
    
    @Override
    public final GeneratedWork get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
                                                                       TimeoutException {
        return future.get(timeout, unit);
    }
    
}
