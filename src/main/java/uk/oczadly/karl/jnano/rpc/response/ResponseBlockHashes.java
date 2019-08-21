package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ResponseBlockHashes extends RpcResponse {

    @Expose
    @SerializedName(value="blocks", alternate={"hashes", "confirmations"})
    private Set<String> blockHashes;
    
    
    
    public Set<String> getBlockHashes() {
        return blockHashes;
    }
    
}
