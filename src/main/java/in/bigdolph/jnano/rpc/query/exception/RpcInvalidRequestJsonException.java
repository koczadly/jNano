package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown if the node was unable to parse the sent request.
 * This is usually an indication that the expected request content has been changed.
 */
public class RpcInvalidRequestJsonException extends RpcException {
    
    public RpcInvalidRequestJsonException() {
        super("The RPC server was unable to parse the JSON request");
    }
    
}
