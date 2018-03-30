package in.bigdolph.jnano.rpc.query.request.compute;

import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.AccountKeyPairResponse;

public class AccountCreateAdhocRequest extends RpcRequest<AccountKeyPairResponse> {
    
    public AccountCreateAdhocRequest() {
        super("key_create", AccountKeyPairResponse.class);
    }
    
}
