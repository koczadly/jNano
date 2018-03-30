package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class NodePeerBootstrapInitializeRequest extends RpcRequest<RpcResponse> {
    
    public NodePeerBootstrapInitializeRequest() {
        super("bootstrap_any", RpcResponse.class);
    }
    
}
