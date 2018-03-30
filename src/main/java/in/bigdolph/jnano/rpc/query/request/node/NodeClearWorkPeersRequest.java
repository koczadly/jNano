package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class NodeClearWorkPeersRequest extends RPCRequest<RPCResponse> {
    
    public NodeClearWorkPeersRequest() {
        super("work_peers_clear", RPCResponse.class);
    }
    
}
