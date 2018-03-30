package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.BlockCountByTypeResponse;

public class BlockCountByTypeRequest extends RPCRequest<BlockCountByTypeResponse> {
    
    public BlockCountByTypeRequest() {
        super("block_count_type", BlockCountByTypeResponse.class);
    }
    
}
