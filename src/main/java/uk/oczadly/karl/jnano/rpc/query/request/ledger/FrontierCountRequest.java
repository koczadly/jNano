package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.CountResponse;

public class FrontierCountRequest extends RpcRequest<CountResponse> {
    
    public FrontierCountRequest() {
        super("frontier_count", CountResponse.class);
    }
    
}
