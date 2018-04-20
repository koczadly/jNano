package uk.oczadly.karl.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.AccountKeyPairResponse;

public class AccountKeyExpandRequest extends RpcRequest<AccountKeyPairResponse> {
    
    @Expose
    @SerializedName("key")
    private String privateKey;
    
    
    public AccountKeyExpandRequest(String privateKey) {
        super("key_expand", AccountKeyPairResponse.class);
        this.privateKey = privateKey;
    }
    
    
    
    public String getPrivateKey() {
        return privateKey;
    }
    
}
