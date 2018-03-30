package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.BlockCountByTypeResponse;

public class BlockCountByTypeRequest extends RpcRequest<BlockCountByTypeResponse> {
    
    public BlockCountByTypeRequest() {
        super("block_count_type", BlockCountByTypeResponse.class);
    }
    
}
