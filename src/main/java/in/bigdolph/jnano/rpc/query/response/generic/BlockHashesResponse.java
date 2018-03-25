package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.util.Set;

public class BlockHashesResponse extends RPCResponse {

    @Expose
    @SerializedName(value="blocks", alternate={"hashes"})
    private Set<String> blockHashes;
    
    
    
    public Set<String> getBlockHashes() {
        return blockHashes;
    }
    
}
