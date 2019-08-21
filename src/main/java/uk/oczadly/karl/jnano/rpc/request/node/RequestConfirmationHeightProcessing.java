package uk.oczadly.karl.jnano.rpc.request.node;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

public class RequestConfirmationHeightProcessing extends RpcRequest<ResponseBlockHash> {
    
    public RequestConfirmationHeightProcessing() {
        super("confirmation_height_currently_processing", ResponseBlockHash.class);
    }
    
}
