package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class NodeClearWorkPeersRequest extends RpcRequest<RpcResponse> {
    
    public NodeClearWorkPeersRequest() {
        super("work_peers_clear", RpcResponse.class);
    }
    
}
