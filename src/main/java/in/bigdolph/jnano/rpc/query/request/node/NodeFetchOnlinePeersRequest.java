package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.NodeFetchOnlinePeersResponse;

public class NodeFetchOnlinePeersRequest extends RPCRequest<NodeFetchOnlinePeersResponse> {
    
    public NodeFetchOnlinePeersRequest() {
        super("peers", NodeFetchOnlinePeersResponse.class);
    }
    
}
