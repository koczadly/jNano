package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.NodeMinimumReceiveResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class NodeMinimumReceiveRequest extends RPCRequest<NodeMinimumReceiveResponse> {
    
    public NodeMinimumReceiveRequest() {
        super("receive_minimum", NodeMinimumReceiveResponse.class);
    }
    
}
