package in.bigdolph.jnano.rpc.query.exception;

public class RpcInvalidAuthTokenException extends RpcQueryException {
    
    public RpcInvalidAuthTokenException() {
        super("The specified authentication token was rejected by the RPC server");
    }
    
}
