package in.bigdolph.jnano.rpc.query.request.compute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.AccountKeyPairResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class AccountFromSeedRequest extends RPCRequest<AccountKeyPairResponse> {
    
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
