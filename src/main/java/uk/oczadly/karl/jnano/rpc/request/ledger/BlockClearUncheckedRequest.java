package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class BlockClearUncheckedRequest extends RpcRequest<RpcResponse> {
    
    public BlockClearUncheckedRequest() {
        super("unchecked_clear", RpcResponse.class);
    }
    
}
