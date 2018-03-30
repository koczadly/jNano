package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class BlockClearUncheckedRequest extends RPCRequest<RPCResponse> {
    
    public BlockClearUncheckedRequest() {
        super("unchecked_clear", RPCResponse.class);
    }
    
}
