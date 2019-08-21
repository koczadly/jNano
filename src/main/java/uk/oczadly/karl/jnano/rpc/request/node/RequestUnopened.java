package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUnopened;

public class RequestUnopened extends RpcRequest<ResponseUnopened> {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    public RequestUnopened(String account, int count) {
        super("unopened", ResponseUnopened.class);
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
