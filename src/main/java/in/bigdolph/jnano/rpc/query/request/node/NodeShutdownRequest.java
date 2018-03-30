package in.bigdolph.jnano.rpc.query.request.node;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class NodeShutdownRequest extends RpcRequest<RpcResponse> {
    
    public NodeShutdownRequest() {
        super("stop", RpcResponse.class);
    }
    
}
