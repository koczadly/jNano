package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

import java.util.Set;

/**
 * This response class contains a series of block hashes.
 */
public class ResponseBlockHashes extends RpcResponse {

    @Expose @SerializedName(value="blocks", alternate={"hashes", "confirmations"})
    private Set<String> blockHashes;
    
    
    /**
     * @return a set of block hashes
     */
    public Set<String> getBlockHashes() {
        return blockHashes;
    }
    
}
