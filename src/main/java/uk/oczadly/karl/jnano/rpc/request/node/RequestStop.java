package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to shutdown the node.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code stop}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#stop">Official RPC documentation</a>
 */
public class RequestStop extends RpcRequest<ResponseSuccessful> {
    
    public RequestStop() {
        super("stop", ResponseSuccessful.class);
    }
    
}
