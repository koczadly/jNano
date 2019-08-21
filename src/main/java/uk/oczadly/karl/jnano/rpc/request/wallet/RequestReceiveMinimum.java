package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeMinimumReceiveResponse;
import uk.oczadly.karl.jnano.rpc.response.ResponseAmount;

public class RequestReceiveMinimum extends RpcRequest<ResponseAmount> {
    
    public RequestReceiveMinimum() {
        super("receive_minimum", ResponseAmount.class);
    }
    
}
