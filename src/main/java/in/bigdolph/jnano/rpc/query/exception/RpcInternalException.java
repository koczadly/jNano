package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown if the node encounters an internal error.
 */
public class RpcInternalException extends RpcException {
    
    public RpcInternalException(String message) {
        super(message);
    }
    
}
