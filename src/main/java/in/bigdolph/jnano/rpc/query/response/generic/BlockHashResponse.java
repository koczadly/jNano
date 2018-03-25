package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class BlockHashResponse extends RPCResponse {

    @Expose
    @SerializedName(value="block", alternate={"hash"})
    private String blockHash;
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
