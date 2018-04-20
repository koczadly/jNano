package uk.oczadly.karl.jnano.rpc.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountKeyPairResponse;

public class AccountFromSeedRequest extends RpcRequest<AccountKeyPairResponse> {
    
    @Expose
    @SerializedName("seed")
    private String seed;
    
    @Expose
    @SerializedName("index")
    private int accountIndex;
    
    
    public AccountFromSeedRequest(String seed, int accountIndex) {
        super("deterministic_key", AccountKeyPairResponse.class);
        this.seed = seed;
        this.accountIndex = accountIndex;
    }
    
    
    
    public String getSeed() {
        return seed;
    }
    
    public int getAccountIndex() {
        return accountIndex;
    }
    
}
