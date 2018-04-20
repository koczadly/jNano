package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class NodeClearWorkPeersRequest extends RpcRequest<RpcResponse> {
    
    public NodeClearWorkPeersRequest() {
        super("work_peers_clear", RpcResponse.class);
    }
    
}
