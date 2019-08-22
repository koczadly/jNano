package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown if the node does not understand the request command.
 */
public class RpcUnknownCommandException extends RpcException {
    
    public RpcUnknownCommandException() {
        super("An invalid request command was sent to the RPC server");
    }
    
}
