package uk.oczadly.karl.jnano.exception;

/**
 * Thrown if one of the referenced entities (eg. account, wallet, block hash) do not exist.
 */
public class RpcEntityNotFoundException extends RpcException {
    
    public RpcEntityNotFoundException(String message) {
        super(message);
    }
    
}
