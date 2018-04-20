package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockCountResponse;

public class BlockCountRequest extends RpcRequest<BlockCountResponse> {
    
    public BlockCountRequest() {
        super("block_count", BlockCountResponse.class);
    }
    
}
