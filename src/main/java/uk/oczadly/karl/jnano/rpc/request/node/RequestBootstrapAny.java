package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to initialize bootstrap connection to random peers.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code bootstrap_any}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_any">Official RPC documentation</a>
 */
public class RequestBootstrapAny extends RpcRequest<ResponseSuccessful> {
    
    public RequestBootstrapAny() {
        super("bootstrap_any", ResponseSuccessful.class);
    }
    
}
