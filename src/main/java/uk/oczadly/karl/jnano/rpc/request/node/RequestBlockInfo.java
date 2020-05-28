package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockInfo;

/**
 * This request class is used to request a block's information.
 * <br>Calls the RPC command {@code block_info}, and returns a {@link ResponseBlockInfo} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_info">Official RPC documentation</a>
 */
public class RequestBlockInfo extends RpcRequest<ResponseBlockInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    
    @Expose @SerializedName("hash")
    private final String hash;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestBlockInfo(String blockHash) {
        super("block_info", ResponseBlockInfo.class);
        this.hash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return hash;
    }
    
}
