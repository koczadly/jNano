package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown if one of the referenced entities (eg. account, wallet, block hash) do not exist.
 */
public class RpcEntityNotFoundException extends RpcException {
    
    public RpcEntityNotFoundException(String message) {
        super(message);
    }
    
}
