package uk.oczadly.karl.jnano.rpc.exception;

/**
 * <p>Thrown when an unsafe request is executed, but is disabled on the node.</p>
 * <p>Set {@code allow_unsafe = true} in the node's configuration to prevent this error.</p>
 */
public class RpcUnsafeNotAllowedException extends RpcFeatureDisabledException {
    
    public RpcUnsafeNotAllowedException() {
        super("The specified command is unsafe and disallowed by the node.");
    }
    
}
