package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;

public class WalletFetchWorkResponse extends RPCResponse {
    
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
