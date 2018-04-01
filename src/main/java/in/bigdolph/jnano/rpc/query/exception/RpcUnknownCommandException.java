package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown if the node does not understand the request command.
 */
public class RpcUnknownCommandException extends RpcQueryException {
    
    public RpcUnknownCommandException() {
        super("An invalid request command was sent to the RPC server");
    }
    
}
