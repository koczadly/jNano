package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockCountByTypeResponse;

public class BlockCountByTypeRequest extends RpcRequest<BlockCountByTypeResponse> {
    
    public BlockCountByTypeRequest() {
        super("block_count_type", BlockCountByTypeResponse.class);
    }
    
}
