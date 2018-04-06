package in.bigdolph.jnano.rpc.query.request.network;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountsResponse;
import in.bigdolph.jnano.rpc.query.response.NodeVersionResponse;

public class OnlineRepresentativesRequest extends RpcRequest<AccountsResponse> {
    
    public OnlineRepresentativesRequest() {
        super("representatives_online", AccountsResponse.class);
    }
    
}
