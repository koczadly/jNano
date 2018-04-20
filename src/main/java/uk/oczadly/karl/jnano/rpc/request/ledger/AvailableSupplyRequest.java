package uk.oczadly.karl.jnano.rpc.request.ledger;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AvailableSupplyResponse;

public class AvailableSupplyRequest extends RpcRequest<AvailableSupplyResponse> {
    
    public AvailableSupplyRequest() {
        super("available_supply", AvailableSupplyResponse.class);
    }
    
}
