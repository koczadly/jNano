/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.service.workgen.policy;

/**
 * This exception is thrown when work difficulty values could not be retrieved.
 */
public class DifficultyRetrievalException extends Exception {
    
    private final boolean cancelled;
    
    public DifficultyRetrievalException(String message, Throwable cause) {
        super(message, cause);
        this.cancelled = false;
    }
    
    public DifficultyRetrievalException(Throwable cause) {
        super(cause);
        this.cancelled = false;
    }
    
    public DifficultyRetrievalException(String message) {
        this(message, false);
    }
    
    public DifficultyRetrievalException(String message, boolean cancelled) {
        super(message);
        this.cancelled = cancelled;
    }
    
    
    public boolean wasCancelled() {
        return cancelled;
    }
    
}
