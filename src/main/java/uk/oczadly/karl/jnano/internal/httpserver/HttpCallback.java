/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.httpserver;

public interface HttpCallback {
    
    void onRequest(HttpRequest request);
    
}
