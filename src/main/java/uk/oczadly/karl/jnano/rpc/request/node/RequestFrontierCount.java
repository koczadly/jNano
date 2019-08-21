package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseCount;

public class RequestFrontierCount extends RpcRequest<ResponseCount> {
    
    public RequestFrontierCount() {
        super("frontier_count", ResponseCount.class);
    }
    
}
