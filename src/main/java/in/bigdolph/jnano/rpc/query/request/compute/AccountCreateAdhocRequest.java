package in.bigdolph.jnano.rpc.query.request.compute;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.AccountKeyPairResponse;

public class AccountCreateAdhocRequest extends RPCRequest<AccountKeyPairResponse> {
    
    public AccountCreateAdhocRequest() {
        super("key_create", AccountKeyPairResponse.class);
    }
    
}
