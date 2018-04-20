package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class SearchPendingRequest extends RpcRequest<RpcResponse> {
    
    public SearchPendingRequest() {
        super("search_pending_all", RpcResponse.class);
    }
    
}
