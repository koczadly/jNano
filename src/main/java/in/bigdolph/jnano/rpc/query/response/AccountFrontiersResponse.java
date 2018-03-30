package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.util.Map;
import java.util.Set;

public class AccountFrontiersResponse extends RPCResponse {
    
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
