package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

/**
 * This request class is used to request the total available supply of Nano.
 * <br>Calls the RPC command {@code available_supply}, and returns a {@link ResponseAmount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#available_supply">Official RPC documentation</a>
 */
public class RequestAvailableSupply extends RpcRequest<ResponseAmount> {
    
    public RequestAvailableSupply() {
        super("available_supply", ResponseAmount.class);
    }
    
}
