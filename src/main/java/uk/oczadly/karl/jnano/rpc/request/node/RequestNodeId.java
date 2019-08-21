package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseNodeId;

public class RequestNodeId extends RpcRequest<ResponseNodeId> {
    
    public RequestNodeId() {
        super("node_id", ResponseNodeId.class);
    }
    
}
