package uk.oczadly.karl.jnano.rpc.request.network;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountsResponse;
import uk.oczadly.karl.jnano.rpc.response.ConfirmationHistoryResponse;

public class ConfirmationHistoryRequest extends RpcRequest<ConfirmationHistoryResponse> {
    
    public ConfirmationHistoryRequest() {
        super("confirmation_history", ConfirmationHistoryResponse.class);
    }
    
}
