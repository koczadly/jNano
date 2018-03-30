package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.WalletResponse;

public class WalletCreateRequest extends RpcRequest<WalletResponse> {
    
    public WalletCreateRequest() {
        super("wallet_create", WalletResponse.class);
    }
    
}
