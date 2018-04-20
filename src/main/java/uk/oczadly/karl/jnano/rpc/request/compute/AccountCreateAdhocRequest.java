package uk.oczadly.karl.jnano.rpc.request.compute;

import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountKeyPairResponse;

public class AccountCreateAdhocRequest extends RpcRequest<AccountKeyPairResponse> {
    
    public AccountCreateAdhocRequest() {
        super("key_create", AccountKeyPairResponse.class);
    }
    
}
