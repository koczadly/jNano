package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class SearchPendingRequest extends RpcRequest<RpcResponse> {
    
    public SearchPendingRequest() {
        super("search_pending_all", RpcResponse.class);
    }
    
}
