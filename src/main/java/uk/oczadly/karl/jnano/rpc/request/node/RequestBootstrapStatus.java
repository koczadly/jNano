package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBootstrapStatus;

/**
 * This request class is used to request the status of the current bootstrap attempt.
 * The server responds with a {@link ResponseBootstrapStatus} data object.<br>
 * Calls the internal RPC method {@code bootstrap_status}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_status">Official RPC documentation</a>
 *
 * @deprecated This request is for debugging purposes only and is subject to change with each node revision.
 */
public class RequestBootstrapStatus extends RpcRequest<ResponseBootstrapStatus> {
    
    public RequestBootstrapStatus() {
        super("bootstrap_status", ResponseBootstrapStatus.class);
    }
    
}
