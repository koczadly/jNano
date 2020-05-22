package uk.oczadly.karl.jnano.rpc;

import uk.oczadly.karl.jnano.rpc.exception.RpcException;

import java.io.IOException;

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
     * @param request  the executed request
     */
    void onResponse(R response, Q request);
    
    /**
     * This method is called when an error occurs with the connection to the node. This can include failing to connect
     * to the configured remote node, network disconnections/instability, or if the method surpasses the specified
     * timeout period.
     *
     * @param ex      the exception thrown
     * @param request the executed request
     */
    void onFailure(IOException ex, Q request);
    
    /**
     * This method is called when there is an error with the request data, returned response data, or the node. View the
     * exceptions table on the GitHub wiki for a list of possible exception classes, which you can use (along with an
     * {@code instanceof} check) to determine the detailed cause of the exception.
     *
     * @param ex      the exception thrown
     * @param request the executed request
     * @see <a href="https://github.com/koczadly/jNano/wiki/Query-requests#exceptions-table">Exceptions table on the
     * GitHub Wiki</a>
     */
    void onFailure(RpcException ex, Q request);
    
}
