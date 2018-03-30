package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.CountResponse;

public class FrontierCountRequest extends RpcRequest<CountResponse> {
    
    public FrontierCountRequest() {
        super("frontier_count", CountResponse.class);
    }
    
}
