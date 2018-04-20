package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeVersionResponse;

public class NodeVersionRequest extends RpcRequest<NodeVersionResponse> {
    
    public NodeVersionRequest() {
        super("version", NodeVersionResponse.class);
    }
    
}
