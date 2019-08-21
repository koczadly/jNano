package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestWorkPeersClear extends RpcRequest<ResponseSuccessful> {
    
    public RequestWorkPeersClear() {
        super("work_peers_clear", ResponseSuccessful.class);
    }
    
}
