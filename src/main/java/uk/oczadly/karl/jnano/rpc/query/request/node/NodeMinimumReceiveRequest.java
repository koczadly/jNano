package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.NodeMinimumReceiveResponse;

public class NodeMinimumReceiveRequest extends RpcRequest<NodeMinimumReceiveResponse> {
    
    public NodeMinimumReceiveRequest() {
        super("receive_minimum", NodeMinimumReceiveResponse.class);
    }
    
}
