package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseNodeId;

/**
 * This request class is used to fetch the node's ID and private key.
 * The server responds with a {@link ResponseNodeId} data object.<br>
 * Calls the internal RPC method {@code node_id}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#node_id">Official RPC documentation</a>
 *
 * @deprecated This request is for debugging purposes only and is subject to change with each node revision.
 */
public class RequestNodeId extends RpcRequest<ResponseNodeId> {
    
    public RequestNodeId() {
        super("node_id", ResponseNodeId.class);
    }
    
}
