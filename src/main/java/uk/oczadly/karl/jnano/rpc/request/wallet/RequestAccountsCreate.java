package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;

public class RequestAccountsCreate extends RpcRequest<ResponseAccounts> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("work")
    private Boolean generateWork;
    
    
    public RequestAccountsCreate(String walletId, int count) {
        this(walletId, count, null);
    }
    
    public RequestAccountsCreate(String walletId, int count, Boolean generateWork) {
        super("accounts_create", ResponseAccounts.class);
        this.walletId = walletId;
        this.count = count;
        this.generateWork = generateWork;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public int getCount() {
        return count;
    }
    
    public boolean getGenerateWork() {
        return generateWork;
    }
    
}
