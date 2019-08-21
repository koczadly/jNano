package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestWalletAdd extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("key")
    private String key;
    
    @Expose @SerializedName("work")
    private Boolean generateWork;
    
    
    public RequestWalletAdd(String walletId, String key) {
        this(walletId, key, null);
    }
    
    public RequestWalletAdd(String walletId, String key, Boolean generateWork) {
        super("wallet_add", ResponseAccount.class);
        this.walletId = walletId;
        this.key = key;
        this.generateWork = generateWork;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getKey() {
        return key;
    }
    
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
