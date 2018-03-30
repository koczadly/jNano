package in.bigdolph.jnano.rpc.query.exception;

public class RpcWalletLockedException extends RpcQueryException {
    
    public RpcWalletLockedException() {
        super("The accessed wallet is currently locked");
    }
    
}
