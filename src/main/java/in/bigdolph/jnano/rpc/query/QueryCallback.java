package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.query.exception.RpcQueryException;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

import java.io.IOException;

/**
 * This class is used for handling asynchronous RPC queries.
 *
 * @param <R> the response class
 */
public interface QueryCallback<R extends RpcResponse> {
    
    /**
     * This method is called when the node returns a successful and valid response.
     *
     * @param response  the response returned from the RPC node
     */
    void onResponse(R response);
    
    /**
     * This method is called if there is an error processing the query.
     * Common exceptions caught by the executor are {@link RpcQueryException} and {@link IOException}.
     *
     * @param ex    the exception thrown
     */
    void onFailure(Exception ex);
    
}
