package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.NodeWorkPeersResponse;

public class NodeFetchWorkPeersRequest extends RPCRequest<NodeWorkPeersResponse> {
    
    public NodeFetchWorkPeersRequest() {
        super("work_peers", NodeWorkPeersResponse.class);
    }
    
}
