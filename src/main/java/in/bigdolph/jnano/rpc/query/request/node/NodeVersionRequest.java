package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.NodeVersionResponse;

public class NodeVersionRequest extends RpcRequest<NodeVersionResponse> {
    
    public NodeVersionRequest() {
        super("version", NodeVersionResponse.class);
    }
    
}
