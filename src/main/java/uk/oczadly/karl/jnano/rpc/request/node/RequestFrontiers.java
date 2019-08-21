package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

public class RequestFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    public RequestFrontiers(String account, int count) {
        super("frontiers", ResponseMultiAccountFrontiers.class);
        this.account = account;
        this.count = count;
    }
    
    
    public String getAccount() {
        return account;
    }
    
    public int getCount() {
        return count;
    }
    
}
