package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestNodeIdDelete extends RpcRequest<ResponseSuccessful> {
    
    public RequestNodeIdDelete() {
        super("node_id_delete", ResponseSuccessful.class);
    }
    
}
