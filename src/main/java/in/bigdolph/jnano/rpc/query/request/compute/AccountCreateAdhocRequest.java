package in.bigdolph.jnano.rpc.query.request.compute;

import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountKeyPairResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountCreateAdhocRequest extends RPCRequest<AccountKeyPairResponse> {
    
    public AccountCreateAdhocRequest() {
        super("key_create", AccountKeyPairResponse.class);
    }
    
}
