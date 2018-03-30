package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountResponse extends RpcResponse {

    @Expose
    @SerializedName("count")
    private long count;
    
    
    
    public long getCount() {
        return count;
    }
    
}
