package in.bigdolph.jnano.rpc.query.exception;

public class RpcInvalidAuthTokenException extends RpcQueryException {
    
    public RpcInvalidAuthTokenException() {
        super("An invalid authentication token was rejected by the node");
    }
    
}
