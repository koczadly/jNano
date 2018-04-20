package uk.oczadly.karl.jnano.exception;

/**
 * Thrown when a query needs access to a wallet which is locked.
 */
public class RpcWalletLockedException extends RpcException {
    
    public RpcWalletLockedException() {
        super("The accessed wallet is currently locked");
    }
    
}
