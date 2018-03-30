package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class BlockClearUncheckedRequest extends RpcRequest<RpcResponse> {
    
    public BlockClearUncheckedRequest() {
        super("unchecked_clear", RpcResponse.class);
    }
    
}
