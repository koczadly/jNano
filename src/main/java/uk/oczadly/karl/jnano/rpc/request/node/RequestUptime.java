package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUptime;

public class RequestUptime extends RpcRequest<ResponseUptime> {
    
    public RequestUptime() {
        super("uptime", ResponseUptime.class);
    }
    
}
