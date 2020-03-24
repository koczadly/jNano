package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to manually search for pending blocks in all local wallet accounts.
 * <br>Calls the RPC command {@code search_pending_all}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#search_pending_all">Official RPC documentation</a>
 */
public class RequestSearchPendingAll extends RpcRequest<ResponseSuccessful> {
    
    public RequestSearchPendingAll() {
        super("search_pending_all", ResponseSuccessful.class);
    }
    
}
