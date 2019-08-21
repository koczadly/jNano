package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiBlockInfo;

public class RequestMultiBlocksInfo extends RpcRequest<ResponseMultiBlockInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    @Expose @SerializedName("source")
    private final boolean fetchSource = true;
    
    @Expose @SerializedName("balance")
    private final boolean fetchBalance = true;
    
    @Expose @SerializedName("include_not_found")
    private Boolean includeNotFound;
    
    @Expose @SerializedName("hashes")
    private String[] blockHashes;
    
    
    public RequestMultiBlocksInfo(String... blockHashes) {
        this(null, blockHashes);
    }
    
    public RequestMultiBlocksInfo(Boolean includeNotFound, String... blockHashes) {
        super("blocks_info", ResponseMultiBlockInfo.class);
        this.blockHashes = blockHashes;
        this.includeNotFound = includeNotFound;
    }
    
    
    public String[] getBlockHashes() {
        return blockHashes;
    }
    
}
