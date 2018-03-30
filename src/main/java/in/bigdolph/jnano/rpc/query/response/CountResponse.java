package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class CountResponse extends RPCResponse {

    @Expose
    @SerializedName("count")
    private long count;
    
    
    
    public long getCount() {
        return count;
    }
    
}
