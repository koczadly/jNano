package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AccountBlockCountResponse extends RPCResponse {

    @Expose
    @SerializedName("block_count")
    private long blockCount;
    
    
    
    public long getBlockCount() {
        return blockCount;
    }
    
}
