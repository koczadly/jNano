package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkPeers;

/**
 * This request class is used to fetch a list of configured work peers on the node.
 * <br>Calls the RPC command {@code work_peers}, and returns a {@link ResponseWorkPeers} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_peers">Official RPC documentation</a>
 */
public class RequestWorkPeers extends RpcRequest<ResponseWorkPeers> {
    
    public RequestWorkPeers() {
        super("work_peers", ResponseWorkPeers.class);
    }
    
}
