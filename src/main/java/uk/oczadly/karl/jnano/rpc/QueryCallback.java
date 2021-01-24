/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

import java.io.IOException;

/**
 * This class is used for handling asynchronous RPC queries. The appropriate methods are automatically invoked from a
 * separate worker thread on the completion or failure of an RPC request submitted along with this callback instance.
 *
 * @param <Q> the request class
 * @param <R> the response class
 */
public interface QueryCallback<Q extends RpcRequest<? extends R>, R extends RpcResponse> {
    
    /**
     * This method is called when the request was successful and the node has returned a valid response.
     *
     * @param response the response returned from the RPC node
     * @param request  the request which resulted in this response
     */
    void onResponse(R response, Q request);
    
    /**
     * This method is called when there is an error with the request data, returned response data, or the node.
     *
     * <p>View the exceptions table on the GitHub wiki for a list of possible exception classes, which you can use
     * (along with an {@code instanceof} check) to determine the detailed cause of the exception.</p>
     *
     * @param ex      the RPC exception thrown
     * @param request the attempted request which triggered this exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#exceptions-table">Exceptions table on the
     * GitHub Wiki</a>
     */
    void onFailure(RpcException ex, Q request);
    
    /**
     * This method is called when an exception occurs with the connection to the node.
     *
     * @param ex      the IO exception thrown
     * @param request the attempted request which triggered this exception
     */
    void onFailure(IOException ex, Q request);
    
}
