package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to delete the node's current ID (requires a restart).
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code node_id_delete}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#node_id_delete">Official RPC documentation</a>
 *
 * @deprecated This request is for debugging purposes only and is subject to change with each node revision.
 */
public class RequestNodeIdDelete extends RpcRequest<ResponseSuccessful> {
    
    public RequestNodeIdDelete() {
        super("node_id_delete", ResponseSuccessful.class);
    }
    
}
