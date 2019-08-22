package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

/**
 * This request class is used to request the total available supply of Nano.
 * The server responds with a {@link ResponseAmount} data object.<br>
 * Calls the internal RPC method {@code available_supply}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#available_supply">Official RPC documentation</a>
 */
public class RequestAvailableSupply extends RpcRequest<ResponseAmount> {
    
    public RequestAvailableSupply() {
        super("available_supply", ResponseAmount.class);
    }
    
}
