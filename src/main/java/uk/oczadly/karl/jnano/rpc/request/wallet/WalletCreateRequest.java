package uk.oczadly.karl.jnano.rpc.request.wallet;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.WalletResponse;

public class WalletCreateRequest extends RpcRequest<WalletResponse> {
    
    public WalletCreateRequest() {
        super("wallet_create", WalletResponse.class);
    }
    
}
