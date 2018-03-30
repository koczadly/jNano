package in.bigdolph.jnano.rpc.query.exception;

public class RpcControlDisabledException extends RpcQueryException {
    
    public RpcControlDisabledException() {
        super("The specified command requires control to be enabled on the RPC server");
    }
    
}
