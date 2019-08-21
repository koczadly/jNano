package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestStop extends RpcRequest<ResponseSuccessful> {
    
    public RequestStop() {
        super("stop", ResponseSuccessful.class);
    }
    
}
