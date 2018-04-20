package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AccountFrontiersResponse extends RpcResponse {
    
    @Expose
    @SerializedName("frontiers")
    private Map<String, String> frontiers;
    
    
    
    public Map<String, String> getFrontiers() {
        return frontiers;
    }
    
    public String getFrontierBlockHash(String accountAddress) {
        return this.frontiers.get(accountAddress.toLowerCase());
    }
    
}
