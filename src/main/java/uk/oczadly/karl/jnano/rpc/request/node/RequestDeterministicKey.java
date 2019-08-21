package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

public class RequestDeterministicKey extends RpcRequest<ResponseKeyPair> {
    
    @Expose @SerializedName("seed")
    private String seed;
    
    @Expose @SerializedName("index")
    private int accountIndex;
    
    
    public RequestDeterministicKey(String seed, int accountIndex) {
        super("deterministic_key", ResponseKeyPair.class);
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
