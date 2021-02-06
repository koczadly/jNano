/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import java.io.IOException;

/**
 * Classes which implement this interface are responsible for submitting requests and retrieving the responses from an
 * external Nano node.
 */
public interface RpcRequestExecutor {
    
    /**
     * Submits a raw RPC request to the external node.
     * @param request   the raw request data
     * @param timeout   the timeout value in milliseconds, or 0 for infinite
     * @return the raw string response
     * @throws IOException if an exception occurs with the remote connection
     */
    String submit(String request, int timeout) throws IOException;
    
}
