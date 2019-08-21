package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

public class RequestChain extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("offset")
    private Integer offset;
    
    @Expose @SerializedName("reverse")
    private Boolean reverse;
    
    
    public RequestChain(String blockHash, int count) {
        this(blockHash, count, null);
    }
    
    public RequestChain(String blockHash, int count, Integer offset) {
        this(blockHash, count, offset, null);
    }
    
    public RequestChain(String blockHash, int count, Integer offset, Boolean reverse) {
        super("chain", ResponseBlockHashes.class);
        this.blockHash = blockHash;
        this.count = count;
        this.offset = offset;
        this.reverse = reverse;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public int getCount() {
        return count;
    }
    
    public Integer getOffset() {
        return offset;
    }
    
    public Boolean getReverse() {
        return reverse;
    }
    
}
