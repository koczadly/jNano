package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockCountByTypeResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockCountByTypeRequest extends RPCRequest<BlockCountByTypeResponse> {
    
    public BlockCountByTypeRequest() {
        super("block_count_type", BlockCountByTypeResponse.class);
    }
    
}
