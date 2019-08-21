package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

public class RequestRepublish extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("count")
    private Integer count;
    
    @Expose @SerializedName("sources")
    private Integer sourcesDepth;
    
    @Expose @SerializedName("destinations")
    private Integer destinationsDepth;
    
    
    public RequestRepublish(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    public RequestRepublish(String blockHash, Integer count) {
        this(blockHash, count, null, null);
    }
    
    public RequestRepublish(String blockHash, Integer count, Integer sourcesDepth, Integer destinationsDepth) {
        super("republish", ResponseBlockHashes.class);
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
