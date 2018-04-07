package in.bigdolph.jnano.rpc.query.exception;

/**
 * The default base RPC exception.
 * This is thrown when there is an error with the request or node.
 */
public class RpcException extends Exception {
    
    public RpcException(String message) {
        super(message);
    }
    
}
