package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;

/**
 * This request class is used to retrieve the node's version information.
 * The server responds with a {@link ResponseVersion} data object.<br>
 * Calls the internal RPC method {@code version}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#version">Official RPC documentation</a>
 */
public class RequestVersion extends RpcRequest<ResponseVersion> {
    
    public RequestVersion() {
        super("version", ResponseVersion.class);
    }
    
}
