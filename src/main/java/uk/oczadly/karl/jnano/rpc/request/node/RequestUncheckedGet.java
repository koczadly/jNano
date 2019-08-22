package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlock;

/**
 * This request class is used to fetch an unchecked block by its hash.
 * The server responds with a {@link ResponseBlock} data object.<br>
 * Calls the internal RPC method {@code unchecked_get}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked_get">Official RPC documentation</a>
 */
public class RequestUncheckedGet extends RpcRequest<ResponseBlock> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    /**
     * @param blockHash the hash of the block
     */
    public RequestUncheckedGet(String blockHash) {
        super("unchecked_get", ResponseBlock.class);
        this.blockHash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
