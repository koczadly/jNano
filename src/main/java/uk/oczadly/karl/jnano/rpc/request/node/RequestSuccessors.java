package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

public class RequestSuccessors extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("block")
    private String blockHash;
    
    @Expose @SerializedName("count")
    private int count;
    
    @Expose @SerializedName("offset")
    private Integer offset;
    
    @Expose @SerializedName("reverse")
    private Boolean reverse;
    
    
    public RequestSuccessors(String blockHash, int count) {
        this(blockHash, count, null, null);
    }
    
    public RequestSuccessors(String blockHash, int count, Integer offset, Boolean reverse) {
        super("successors", ResponseBlockHashes.class);
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
