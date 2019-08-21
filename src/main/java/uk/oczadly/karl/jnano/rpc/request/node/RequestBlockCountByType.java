package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCountByType;

public class RequestBlockCountByType extends RpcRequest<ResponseBlockCountByType> {
    
    public RequestBlockCountByType() {
        super("block_count_type", ResponseBlockCountByType.class);
    }
    
}
