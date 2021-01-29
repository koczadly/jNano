/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.work.generator;

import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.concurrent.CompletableFuture;

/**
 * @author Karl Oczadly
 */
public class FutureWork extends CompletableFuture<WorkSolution> {
    
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }
}
