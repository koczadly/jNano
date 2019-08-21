package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBootstrapStatus;

/**
 * @deprecated This request is for internal debugging purposes only, and is subject to change with each node revision.
 */
public class RequestBootstrapStatus extends RpcRequest<ResponseBootstrapStatus> {
    
    public RequestBootstrapStatus() {
        super("bootstrap_status", ResponseBootstrapStatus.class);
    }
    
}
