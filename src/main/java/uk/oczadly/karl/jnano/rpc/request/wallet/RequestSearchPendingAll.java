package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestSearchPendingAll extends RpcRequest<ResponseSuccessful> {
    
    public RequestSearchPendingAll() {
        super("search_pending_all", ResponseSuccessful.class);
    }
    
}
