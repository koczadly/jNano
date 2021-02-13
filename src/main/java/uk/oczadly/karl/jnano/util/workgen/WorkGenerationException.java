/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

/**
 * General exception type for work generation failures.
 */
public class WorkGenerationException extends Exception {
    
    public WorkGenerationException() {
        super();
    }
    
    public WorkGenerationException(String message) {
        super(message);
    }
    
    public WorkGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WorkGenerationException(Throwable cause) {
        super(cause);
    }
    
}
