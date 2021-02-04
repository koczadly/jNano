/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.epoch;

/**
 * This exception is to be thrown when an epoch upgrade could not be recognized from a given version or identifier.
 */
public final class UnrecognizedEpochException extends RuntimeException {
    
    public UnrecognizedEpochException() {}
    
    public UnrecognizedEpochException(String message) {
        super(message);
    }
    
    public UnrecognizedEpochException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UnrecognizedEpochException(Throwable cause) {
        super(cause);
    }
    
}
