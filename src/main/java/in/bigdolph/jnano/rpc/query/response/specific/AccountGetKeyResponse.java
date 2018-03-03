package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class AccountGetKeyResponse extends RPCResponse {

    @Expose
    @SerializedName("key")
    private String key;
    
    
    
    public String getPublicKey() {
        return key;
    }
    
}
