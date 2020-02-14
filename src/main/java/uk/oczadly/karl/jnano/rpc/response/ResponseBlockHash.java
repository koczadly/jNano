package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a single block hash.
 */
public class ResponseBlockHash extends RpcResponse {

    @Expose @SerializedName(value="block", alternate={"hash"})
    private String blockHash;
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
