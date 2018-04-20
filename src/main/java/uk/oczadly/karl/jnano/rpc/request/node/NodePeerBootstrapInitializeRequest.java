package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class NodePeerBootstrapInitializeRequest extends RpcRequest<RpcResponse> {
    
    public NodePeerBootstrapInitializeRequest() {
        super("bootstrap_any", RpcResponse.class);
    }
    
}
