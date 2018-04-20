package uk.oczadly.karl.jnano.rpc.query.request.node;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.NodeVersionResponse;

public class NodeVersionRequest extends RpcRequest<NodeVersionResponse> {
    
    public NodeVersionRequest() {
        super("version", NodeVersionResponse.class);
    }
    
}
