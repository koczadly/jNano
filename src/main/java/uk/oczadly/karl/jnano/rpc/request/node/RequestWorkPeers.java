package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkPeers;

/**
 * This request class is used to fetch a list of configured work peers on the node.
 * The server responds with a {@link ResponseWorkPeers} data object.<br>
 * Calls the internal RPC method {@code work_peers}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_peers">Official RPC documentation</a>
 */
public class RequestWorkPeers extends RpcRequest<ResponseWorkPeers> {
    
    public RequestWorkPeers() {
        super("work_peers", ResponseWorkPeers.class);
    }
    
}
