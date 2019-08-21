package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseVersion;

public class RequestVersion extends RpcRequest<ResponseVersion> {
    
    public RequestVersion() {
        super("version", ResponseVersion.class);
    }
    
}
