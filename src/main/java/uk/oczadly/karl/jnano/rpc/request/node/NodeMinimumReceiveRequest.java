package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeMinimumReceiveResponse;

public class NodeMinimumReceiveRequest extends RpcRequest<NodeMinimumReceiveResponse> {
    
    public NodeMinimumReceiveRequest() {
        super("receive_minimum", NodeMinimumReceiveResponse.class);
    }
    
}
