package uk.oczadly.karl.jnano.exception;

/**
 * Thrown if one of the given request arguments are not valid.
 */
public class RpcInvalidArgumentException extends RpcException {
    
    public RpcInvalidArgumentException(String message) {
        super(message);
    }
    
}
