package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.CountResponse;

public class FrontierCountRequest extends RpcRequest<CountResponse> {
    
    public FrontierCountRequest() {
        super("frontier_count", CountResponse.class);
    }
    
}
