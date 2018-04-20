package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountInformationResponse;

public class AccountInformationRequest extends RpcRequest<AccountInformationResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("representative")
    private boolean fetchRepresentative = true;
    
    @Expose
    @SerializedName("weight")
    private boolean fetchWeight = true;
    
    @Expose
    @SerializedName("pending")
    private boolean fetchPending = true;
    
    
    public AccountInformationRequest(String account) {
        super("account_info", AccountInformationResponse.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
