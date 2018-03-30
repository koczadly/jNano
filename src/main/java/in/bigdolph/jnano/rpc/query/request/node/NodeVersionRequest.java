package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.NodeVersionResponse;

public class NodeVersionRequest extends RPCRequest<NodeVersionResponse> {
    
    public NodeVersionRequest() {
        super("version", NodeVersionResponse.class);
    }
    
}
