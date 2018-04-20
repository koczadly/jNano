package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class BlockClearUncheckedRequest extends RpcRequest<RpcResponse> {
    
    public BlockClearUncheckedRequest() {
        super("unchecked_clear", RpcResponse.class);
    }
    
}
