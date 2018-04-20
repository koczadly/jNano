package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockCountResponse;

public class BlockCountRequest extends RpcRequest<BlockCountResponse> {
    
    public BlockCountRequest() {
        super("block_count", BlockCountResponse.class);
    }
    
}
