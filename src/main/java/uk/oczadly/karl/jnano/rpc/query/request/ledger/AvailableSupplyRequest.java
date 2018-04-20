package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AvailableSupplyResponse;

public class AvailableSupplyRequest extends RpcRequest<AvailableSupplyResponse> {
    
    public AvailableSupplyRequest() {
        super("available_supply", AvailableSupplyResponse.class);
    }
    
}
