package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUptime;

/**
 * This request class is used to retrieve the current uptime of the node.
 * The server responds with a {@link ResponseUptime} data object.<br>
 * Calls the internal RPC method {@code uptime}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#uptime">Official RPC documentation</a>
 */
public class RequestUptime extends RpcRequest<ResponseUptime> {
    
    public RequestUptime() {
        super("uptime", ResponseUptime.class);
    }
    
}
