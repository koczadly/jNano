package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AvailableSupplyResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AvailableSupplyRequest extends RPCRequest<AvailableSupplyResponse> {
    
    public AvailableSupplyRequest() {
        super("available_supply", AvailableSupplyResponse.class);
    }
    
}
