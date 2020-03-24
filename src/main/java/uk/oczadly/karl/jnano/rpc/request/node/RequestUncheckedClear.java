package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to clear the unchecked synchronized blocks from the local ledger.
 * <br>Calls the RPC command {@code unchecked_clear}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked_clear">Official RPC documentation</a>
 */
public class RequestUncheckedClear extends RpcRequest<ResponseSuccessful> {
    
    public RequestUncheckedClear() {
        super("unchecked_clear", ResponseSuccessful.class);
    }
    
}
