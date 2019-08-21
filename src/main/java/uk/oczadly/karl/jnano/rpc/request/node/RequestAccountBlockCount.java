package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountBlockCount;

public class RequestAccountBlockCount extends RpcRequest<ResponseAccountBlockCount> {
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestAccountBlockCount(String account) {
        super("account_block_count", ResponseAccountBlockCount.class);
        this.account = account;
    }
    
    
    
    public String getAccount() {
        return account;
    }
}
