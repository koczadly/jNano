package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

public class RequestAvailableSupply extends RpcRequest<ResponseAmount> {
    
    public RequestAvailableSupply() {
        super("available_supply", ResponseAmount.class);
    }
    
}
