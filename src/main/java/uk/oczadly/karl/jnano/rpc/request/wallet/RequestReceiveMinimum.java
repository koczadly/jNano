package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

/**
 * This request class is used to get the minimum amount for the node to automatically receive transactions.
 * The server responds with a {@link ResponseAmount} data object.<br>
 * Calls the internal RPC method {@code receive_minimum}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#receive_minimum">Official RPC documentation</a>
 */
public class RequestReceiveMinimum extends RpcRequest<ResponseAmount> {
    
    public RequestReceiveMinimum() {
        super("receive_minimum", ResponseAmount.class);
    }
    
}
