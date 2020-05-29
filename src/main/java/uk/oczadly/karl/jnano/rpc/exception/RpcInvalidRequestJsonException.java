package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node was unable to parse the sent request. This is usually an indication that the expected request
 * content has been changed, or that the JSON depth is over the configured maximum limit.
 */
public class RpcInvalidRequestJsonException extends RpcException {
    
    public RpcInvalidRequestJsonException() {
        this("The RPC server was unable to parse the JSON request.");
    }
    
    public RpcInvalidRequestJsonException(String message) {
        super(message);
    }
    
}
