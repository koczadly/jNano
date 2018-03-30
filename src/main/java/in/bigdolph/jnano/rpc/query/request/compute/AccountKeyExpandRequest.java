package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.AccountKeyPairResponse;

public class AccountKeyExpandRequest extends RPCRequest<AccountKeyPairResponse> {
    
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
