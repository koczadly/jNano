package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.NodeWorkPeersResponse;

public class NodeFetchWorkPeersRequest extends RpcRequest<NodeWorkPeersResponse> {
    
    public NodeFetchWorkPeersRequest() {
        super("work_peers", NodeWorkPeersResponse.class);
    }
    
}
