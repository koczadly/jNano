package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class WalletFetchWorkResponse extends RpcResponse {
    
    @Expose
    @SerializedName("works")
    private Map<String, String> work;
    
    
    
    public Map<String, String> getPrecomputedWork() {
        return work;
    }
    
    public String getPrecomputedWork(String accountAddress) {
        return this.work.get(accountAddress.toLowerCase());
    }
    
}
