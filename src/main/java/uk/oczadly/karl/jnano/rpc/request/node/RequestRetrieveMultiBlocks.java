package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlocksMap;

public class RequestRetrieveMultiBlocks extends RpcRequest<ResponseBlocksMap> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("hashes")
    private String[] blockHashes;
    
    
    public RequestRetrieveMultiBlocks(String... blockHashes) {
        super("blocks", ResponseBlocksMap.class);
        this.blockHashes = blockHashes;
    }
    
    
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
}
