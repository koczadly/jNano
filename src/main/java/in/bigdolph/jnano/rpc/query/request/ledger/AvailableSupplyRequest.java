package in.bigdolph.jnano.rpc.query.request.ledger;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AvailableSupplyResponse;

public class AvailableSupplyRequest extends RpcRequest<AvailableSupplyResponse> {
    
    public AvailableSupplyRequest() {
        super("available_supply", AvailableSupplyResponse.class);
    }
    
}
