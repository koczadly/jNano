package in.bigdolph.jnano.rpc.query.exception;

/**
 * Thrown when a query needs access to a wallet which is locked.
 */
public class RpcWalletLockedException extends RpcQueryException {
    
    public RpcWalletLockedException() {
        super("The accessed wallet is currently locked");
    }
    
}
