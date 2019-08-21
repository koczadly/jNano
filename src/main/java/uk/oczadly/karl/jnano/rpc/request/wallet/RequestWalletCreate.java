package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletId;

public class RequestWalletCreate extends RpcRequest<ResponseWalletId> {
    
    @Expose @SerializedName("seed")
    private String seed;
    
    public RequestWalletCreate() {
        this(null);
    }
    
    public RequestWalletCreate(String seed) {
        super("wallet_create", ResponseWalletId.class);
        this.seed = seed;
    }
    
    
    public String getSeed() {
        return seed;
    }
    
}
