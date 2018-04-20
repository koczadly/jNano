package uk.oczadly.karl.jnano.rpc.query.request.network;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountsResponse;

public class OnlineRepresentativesRequest extends RpcRequest<AccountsResponse> {
    
    public OnlineRepresentativesRequest() {
        super("representatives_online", AccountsResponse.class);
    }
    
}
