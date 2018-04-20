package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeWorkPeersResponse;

public class NodeFetchWorkPeersRequest extends RpcRequest<NodeWorkPeersResponse> {
    
    public NodeFetchWorkPeersRequest() {
        super("work_peers", NodeWorkPeersResponse.class);
    }
    
}
