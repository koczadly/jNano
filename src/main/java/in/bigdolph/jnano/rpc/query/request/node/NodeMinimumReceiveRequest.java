package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.NodeMinimumReceiveResponse;

public class NodeMinimumReceiveRequest extends RpcRequest<NodeMinimumReceiveResponse> {
    
    public NodeMinimumReceiveRequest() {
        super("receive_minimum", NodeMinimumReceiveResponse.class);
    }
    
}
