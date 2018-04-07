package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown when a request needs control access but it's disabled on the node.
 */
public class RpcControlDisabledException extends RpcException {
    
    public RpcControlDisabledException() {
        super("The specified command requires control to be enabled on the RPC server");
    }
    
}
