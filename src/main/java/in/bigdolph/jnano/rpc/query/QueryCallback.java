package in.bigdolph.jnano.rpc.query;

import in.bigdolph.jnano.rpc.exception.RPCQueryException;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.io.IOException;

/**
 * This class is used for handling asynchronous RPC queries.
 *
 * @param <Q> the query (request) class
 * @param <R> the response class
 */
public interface QueryCallback<Q extends RPCRequest<R>, R extends RPCResponse> {
    
    /**
     * This method is called when the node returns a successful and valid response.
     *
     * @param query     the original query sent to the RPC node
     * @param response  the response returned from the RPC node
     */
    void onResponse(Q query, R response);
    
    /**
     * This method is called if there is an error processing the query.
     * Common exceptions caught by the executor are {@link RPCQueryException} and {@link IOException}.
     *
     * @param query the original query sent to the RPC node
     * @param ex    the exception thrown
     */
    void onFailure(Q query, Exception ex);
    
}
