package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class NodePeerBootstrapInitializeRequest extends RpcRequest<RpcResponse> {
    
    public NodePeerBootstrapInitializeRequest() {
        super("bootstrap_any", RpcResponse.class);
    }
    
}
