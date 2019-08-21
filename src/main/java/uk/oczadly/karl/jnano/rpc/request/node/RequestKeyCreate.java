package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

public class RequestKeyCreate extends RpcRequest<ResponseKeyPair> {
    
    public RequestKeyCreate() {
        super("key_create", ResponseKeyPair.class);
    }
    
}
