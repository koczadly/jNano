package uk.oczadly.karl.jnano.rpc.exception;

/**
 * Thrown when a request needs control access but it's disabled on the node.
 */
public class RpcControlDisabledException extends RpcException {
    
    public RpcControlDisabledException() {
        super("The specified command requires control to be enabled on the RPC server");
    }
    
}
