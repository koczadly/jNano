package in.bigdolph.jnano.rpc.query.exception;

public class RpcUnknownCommandException extends RpcQueryException {
    
    public RpcUnknownCommandException() {
        super("An invalid request command was sent to the RPC server");
    }
    
}
