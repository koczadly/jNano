package in.bigdolph.jnano.rpc.query.exception;

public class RpcControlDisabledException extends RpcQueryException {
    
    public RpcControlDisabledException() {
        super("Control commands are disabled on the RPC node");
    }
    
}
