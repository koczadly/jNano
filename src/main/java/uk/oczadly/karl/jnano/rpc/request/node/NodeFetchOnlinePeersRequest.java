package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeFetchOnlinePeersResponse;

public class NodeFetchOnlinePeersRequest extends RpcRequest<NodeFetchOnlinePeersResponse> {
    
    public NodeFetchOnlinePeersRequest() {
        super("peers", NodeFetchOnlinePeersResponse.class);
    }
    
}
