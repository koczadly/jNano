package in.bigdolph.jnano.rpc.query.request.wallet;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.WalletResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class WalletCreateRequest extends RPCRequest<WalletResponse> {
    
    public WalletCreateRequest() {
        super("wallet_create", WalletResponse.class);
    }
    
}
