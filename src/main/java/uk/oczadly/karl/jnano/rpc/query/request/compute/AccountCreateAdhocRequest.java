package uk.oczadly.karl.jnano.rpc.query.request.compute;

import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountKeyPairResponse;

public class AccountCreateAdhocRequest extends RpcRequest<AccountKeyPairResponse> {
    
    public AccountCreateAdhocRequest() {
        super("key_create", AccountKeyPairResponse.class);
    }
    
}
