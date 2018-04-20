package uk.oczadly.karl.jnano.rpc.request.network;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountsResponse;

public class OnlineRepresentativesRequest extends RpcRequest<AccountsResponse> {
    
    public OnlineRepresentativesRequest() {
        super("representatives_online", AccountsResponse.class);
    }
    
}
