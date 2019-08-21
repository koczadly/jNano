package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWorkPeers;

public class RequestWorkPeers extends RpcRequest<ResponseWorkPeers> {
    
    public RequestWorkPeers() {
        super("work_peers", ResponseWorkPeers.class);
    }
    
}
