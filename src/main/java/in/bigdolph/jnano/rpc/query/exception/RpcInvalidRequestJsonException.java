package in.bigdolph.jnano.rpc.query.exception;

public class RpcInvalidRequestJsonException extends RpcQueryException {
    
    public RpcInvalidRequestJsonException() {
        super("The RPC server was unable to parse the JSON request");
    }
    
}
