package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.BlockHashesResponse;

public class BlockRepublishRequest extends RpcRequest<BlockHashesResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    @Expose
    @SerializedName("count")
    private Integer count;
    
    @Expose
    @SerializedName("sources")
    private Integer sourcesDepth;
    
    @Expose
    @SerializedName("destinations")
    private Integer destinationsDepth;
    
    
    public BlockRepublishRequest(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    public BlockRepublishRequest(String blockHash, int count) {
        this(blockHash, count, null, null);
    }
    
    public BlockRepublishRequest(String blockHash, Integer count, Integer sourcesDepth, Integer destinationsDepth) {
        super("republish", BlockHashesResponse.class);
        this.blockHash = blockHash;
        this.count = count;
        this.sourcesDepth = sourcesDepth;
        this.destinationsDepth = destinationsDepth;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public Integer getSourcesDepth() {
        return sourcesDepth;
    }
    
    public Integer getDestinationsDepth() {
        return destinationsDepth;
    }
    
}
