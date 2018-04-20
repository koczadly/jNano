package uk.oczadly.karl.jnano.rpc.query.exception;

/**
 * Thrown when the set authentication token doesn't match the node's.
 */
public class RpcInvalidAuthTokenException extends RpcException {
    
    public RpcInvalidAuthTokenException() {
        super("The specified authentication token was rejected by the RPC server");
    }
    
}
