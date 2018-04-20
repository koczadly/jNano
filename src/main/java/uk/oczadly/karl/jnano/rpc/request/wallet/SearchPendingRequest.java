package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

public class SearchPendingRequest extends RpcRequest<RpcResponse> {
    
    public SearchPendingRequest() {
        super("search_pending_all", RpcResponse.class);
    }
    
}
