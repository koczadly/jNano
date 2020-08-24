package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBootstrapStatus;

/**
 * This request class is used to request the status of the current bootstrap attempt.
 * <br>Calls the RPC command {@code bootstrap_status}, and returns a {@link ResponseBootstrapStatus} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_status">Official RPC documentation</a>
 * @deprecated This request is for debugging purposes only and is subject to change with each node revision.
 */
@Deprecated
public class RequestBootstrapStatus extends RpcRequest<ResponseBootstrapStatus> {
    
    public RequestBootstrapStatus() {
        super("bootstrap_status", ResponseBootstrapStatus.class);
    }
    
}
