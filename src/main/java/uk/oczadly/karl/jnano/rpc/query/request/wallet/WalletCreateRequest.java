package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.WalletResponse;

public class WalletCreateRequest extends RpcRequest<WalletResponse> {
    
    public WalletCreateRequest() {
        super("wallet_create", WalletResponse.class);
    }
    
}
