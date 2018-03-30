package in.bigdolph.jnano.rpc.query.exception;

public class RpcUnknownCommandException extends RpcQueryException {
    
    public RpcUnknownCommandException() {
        super("The RPC node did not understand the request command");
    }
    
}
