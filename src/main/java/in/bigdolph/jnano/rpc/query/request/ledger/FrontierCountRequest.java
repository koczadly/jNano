package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.CountResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class FrontierCountRequest extends RPCRequest<CountResponse> {
    
    public FrontierCountRequest() {
        super("frontier_count", CountResponse.class);
    }
    
}
