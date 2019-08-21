package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestUncheckedClear extends RpcRequest<ResponseSuccessful> {
    
    public RequestUncheckedClear() {
        super("unchecked_clear", ResponseSuccessful.class);
    }
    
}
