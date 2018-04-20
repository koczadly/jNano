package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockCountByTypeResponse;

public class BlockCountByTypeRequest extends RpcRequest<BlockCountByTypeResponse> {
    
    public BlockCountByTypeRequest() {
        super("block_count_type", BlockCountByTypeResponse.class);
    }
    
}
