package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseCount;

/**
 * This request class is used to request the number of accounts in the ledger.
 * <br>Calls the RPC command {@code frontier_count}, and returns a {@link ResponseCount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#frontier_count">Official RPC documentation</a>
 */
public class RequestFrontierCount extends RpcRequest<ResponseCount> {
    
    public RequestFrontierCount() {
        super("frontier_count", ResponseCount.class);
    }
    
}
