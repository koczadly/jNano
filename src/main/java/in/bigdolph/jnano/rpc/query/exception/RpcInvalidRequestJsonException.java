package in.bigdolph.jnano.rpc.query.exception;

public class RpcInvalidRequestJsonException extends RpcQueryException {
    
    public RpcInvalidRequestJsonException() {
        super("An invalid request was sent to the node");
    }
    
}
