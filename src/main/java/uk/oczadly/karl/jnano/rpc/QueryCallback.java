package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;

/**
 * This class is used for handling asynchronous RPC queries. The appropriate methods are automatically invoked from a
 * separate worker thread on the completion or failure of an RPC request submitted along with this callback instance.
 *
 * @param <Q> the request class
 * @param <R> the response class
 */
public interface QueryCallback<Q extends RpcRequest<R>, R extends RpcResponse> {
    
    /**
     * This method is called when the request was successful and the node has returned a valid response.
     *
     * @param response the response returned from the RPC node
     * @param request  the request which resulted in this response
     */
    void onResponse(R response, Q request);
    
    /**
     * This method is called when there is an error with the request data, returned response data, or the node. View the
     * exceptions table on the GitHub wiki for a list of possible exception classes, which you can use (along with an
     * {@code instanceof} check) to determine the detailed cause of the exception.
     *
     * @param ex      the exception thrown
     * @param request the attempted request which triggered this exception
     *
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#exceptions-table">Exceptions table on the
     * GitHub Wiki</a>
     */
    void onRpcFailure(RpcException ex, Q request);
    
    /**
     * This method is called when any non-rpc exception occurs, including {@link java.io.IOException} and
     * {@link InterruptedException}.
     *
     * @param ex      the exception thrown
     * @param request the attempted request which triggered this exception
     */
    void onFailure(Exception ex, Q request);
    
}
