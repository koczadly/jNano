/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

/**
 * @author Karl Oczadly
 */
public interface Functions {
    
    @FunctionalInterface
    interface UncheckedFunction<T, R> {
        R apply(T param) throws Exception;
    }

}
