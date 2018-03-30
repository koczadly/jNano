package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class NodeClearWorkPeersRequest extends RpcRequest<RpcResponse> {
    
    public NodeClearWorkPeersRequest() {
        super("work_peers_clear", RpcResponse.class);
    }
    
}
