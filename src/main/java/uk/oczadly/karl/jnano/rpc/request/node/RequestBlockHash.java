package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to calculate the hash of a block based on the provided data.
 * <br>Calls the RPC command {@code block_hash}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_hash">Official RPC documentation</a>
 * @deprecated Use of {@link Block#getHash()} method is preferred, which will natively calculate the hash without the
 * need of an external node.
 */
@Deprecated
public class RequestBlockHash extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = false;
    
    
    @Expose @SerializedName("block")
    private final String block;
    
    
    /**
     * @param block the block's data
     */
    public RequestBlockHash(Block block) {
        this(block.getJsonString());
    }
    
    /**
     * @param blockJson the block's JSON contents
     */
    public RequestBlockHash(String blockJson) {
        super("block_hash", ResponseBlockHash.class);
        this.block = blockJson;
    }
    
    
    /**
     * @return the block's JSON contents
     */
    public String getBlockJson() {
        return block;
    }
    
}
