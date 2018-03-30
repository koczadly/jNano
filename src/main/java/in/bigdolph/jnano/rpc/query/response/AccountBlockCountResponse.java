package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountBlockCountResponse extends RpcResponse {

    @Expose
    @SerializedName("block_count")
    private long blockCount;
    
    
    
    public long getBlockCount() {
        return blockCount;
    }
    
}
