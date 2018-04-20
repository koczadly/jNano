package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class BlockHashesResponse extends RpcResponse {

    @Expose
    @SerializedName(value="blocks", alternate={"hashes"})
    private Set<String> blockHashes;
    
    
    
    public Set<String> getBlockHashes() {
        return blockHashes;
    }
    
}
