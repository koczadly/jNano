package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockCountResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockCountRequest extends RPCRequest<BlockCountResponse> {
    
    public BlockCountRequest() {
        super("block_count", BlockCountResponse.class);
    }
    
}
