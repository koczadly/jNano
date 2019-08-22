package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHashes;

/**
 * This request class is used to fetch a list of block hashes in the account chain.
 * The server responds with a {@link ResponseBlockHashes} data object.<br>
 * Calls the internal RPC method {@code successors}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#successors">Official RPC documentation</a>
 */
public class RequestSuccessors extends RpcRequest<ResponseBlockHashes> {
    
    @Expose @SerializedName("block")
    private final String blockHash;
    
    @Expose @SerializedName("count")
    private final int count;
    
    @Expose @SerializedName("offset")
    private final Integer offset;
    
    @Expose @SerializedName("reverse")
    private final Boolean reverse;
    
    
    public RequestSuccessors(String blockHash) {
        this(blockHash, null);
    }
    
    /**
     * @param blockHash the starting block hash
     * @param count     (optional) the result limit
     */
    public RequestSuccessors(String blockHash, Integer count) {
        this(blockHash, count, null, null);
    }
    
    /**
     * @param blockHash the starting block hash
     * @param count     (optional) the result limit
     * @param offset    (optional) the offset from the starting hash
     * @param reverse   (optional) whether the listed blocks should be predecessors instead
     */
    public RequestSuccessors(String blockHash, Integer count, Integer offset, Boolean reverse) {
        super("successors", ResponseBlockHashes.class);
        this.blockHash = blockHash;
        this.count = count != null ? count : -1;
        this.offset = offset;
        this.reverse = reverse;
    }
    
    
    /**
     * @return the starting block hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return the result block limit
     */
    public int getCount() {
        return count;
    }
    
    /**
     * @return the starting offset
     */
    public Integer getOffset() {
        return offset;
    }
    
    /**
     * @return whether the results should be predecessors instead
     */
    public Boolean getReverse() {
        return reverse;
    }
    
}
