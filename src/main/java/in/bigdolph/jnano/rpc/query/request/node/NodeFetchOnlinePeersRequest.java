package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.NodeFetchOnlinePeersResponse;

public class NodeFetchOnlinePeersRequest extends RpcRequest<NodeFetchOnlinePeersResponse> {
    
    public NodeFetchOnlinePeersRequest() {
        super("peers", NodeFetchOnlinePeersResponse.class);
    }
    
}
