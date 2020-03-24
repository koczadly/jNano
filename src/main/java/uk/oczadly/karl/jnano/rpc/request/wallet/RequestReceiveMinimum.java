package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

/**
 * This request class is used to get the minimum amount for the node to automatically receive transactions.
 * <br>Calls the RPC command {@code receive_minimum}, and returns a {@link ResponseAmount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#receive_minimum">Official RPC documentation</a>
 */
public class RequestReceiveMinimum extends RpcRequest<ResponseAmount> {
    
    public RequestReceiveMinimum() {
        super("receive_minimum", ResponseAmount.class);
    }
    
}
