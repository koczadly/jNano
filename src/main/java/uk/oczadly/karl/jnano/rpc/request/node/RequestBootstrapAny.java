package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestBootstrapAny extends RpcRequest<ResponseSuccessful> {
    
    public RequestBootstrapAny() {
        super("bootstrap_any", ResponseSuccessful.class);
    }
    
}
