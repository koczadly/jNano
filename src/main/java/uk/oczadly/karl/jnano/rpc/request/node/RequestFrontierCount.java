package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseCount;

/**
 * This request class is used to request the number of accounts in the ledger.
 * The server responds with a {@link ResponseCount} data object.<br>
 * Calls the internal RPC method {@code frontier_count}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#frontier_count">Official RPC documentation</a>
 */
public class RequestFrontierCount extends RpcRequest<ResponseCount> {
    
    public RequestFrontierCount() {
        super("frontier_count", ResponseCount.class);
    }
    
}
