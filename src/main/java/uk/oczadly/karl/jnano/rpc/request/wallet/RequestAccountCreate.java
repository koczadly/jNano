package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestAccountCreate extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("work")
    private Boolean generateWork;
    
    @Expose @SerializedName("work")
    private Integer index;
    
    
    public RequestAccountCreate(String walletId) {
        this(walletId, null, null);
    }
    
    public RequestAccountCreate(String walletId, Integer index) {
        this(walletId, index, null);
    }
    
    public RequestAccountCreate(String walletId, Integer index, Boolean generateWork) {
        super("account_create", ResponseAccount.class);
        this.walletId = walletId;
        this.generateWork = generateWork;
    }
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public boolean getGenerateWork() {
        return generateWork;
    }
    
    public Integer getIndex() {
        return index;
    }
    
}
