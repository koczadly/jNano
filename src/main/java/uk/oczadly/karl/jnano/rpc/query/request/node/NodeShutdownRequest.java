package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class NodeShutdownRequest extends RpcRequest<RpcResponse> {
    
    public NodeShutdownRequest() {
        super("stop", RpcResponse.class);
    }
    
}
