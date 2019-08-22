package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to clear the unchecked synchronized blocks from the local ledger.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code unchecked_clear}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked_clear">Official RPC documentation</a>
 */
public class RequestUncheckedClear extends RpcRequest<ResponseSuccessful> {
    
    public RequestUncheckedClear() {
        super("unchecked_clear", ResponseSuccessful.class);
    }
    
}
