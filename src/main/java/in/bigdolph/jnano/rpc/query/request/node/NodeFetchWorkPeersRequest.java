package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.NodeWorkPeersResponse;

public class NodeFetchWorkPeersRequest extends RpcRequest<NodeWorkPeersResponse> {
    
    public NodeFetchWorkPeersRequest() {
        super("work_peers", NodeWorkPeersResponse.class);
    }
    
}
