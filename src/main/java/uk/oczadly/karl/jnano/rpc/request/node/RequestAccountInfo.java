package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo;

public class RequestAccountInfo extends RpcRequest<ResponseAccountInfo> {
    
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
    
    
    public RequestAccountInfo(String account) {
        super("account_info", ResponseAccountInfo.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
}
