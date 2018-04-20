package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.NodeFetchOnlinePeersResponse;

public class NodeFetchOnlinePeersRequest extends RpcRequest<NodeFetchOnlinePeersResponse> {
    
    public NodeFetchOnlinePeersRequest() {
        super("peers", NodeFetchOnlinePeersResponse.class);
    }
    
}
