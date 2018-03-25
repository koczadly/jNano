package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class SearchPendingRequest extends RPCRequest<RPCResponse> {
    
    public SearchPendingRequest() {
        super("search_pending_all", RPCResponse.class);
    }
    
}
