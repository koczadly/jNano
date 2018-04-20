package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class NodeShutdownRequest extends RpcRequest<RpcResponse> {
    
    public NodeShutdownRequest() {
        super("stop", RpcResponse.class);
    }
    
}
