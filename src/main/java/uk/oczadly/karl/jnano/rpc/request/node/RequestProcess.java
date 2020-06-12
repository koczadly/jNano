package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockHash;

/**
 * This request class is used to manually publish a block to the network.
 * <br>Calls the RPC command {@code process}, and returns a {@link ResponseBlockHash} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#process">Official RPC documentation</a>
 */
public class RequestProcess extends RpcRequest<ResponseBlockHash> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = false;
    
    
    @Expose @SerializedName("block")
    private final String blockJson;
    
    @Expose @SerializedName("force")
    private final boolean force;
    
    @Expose @SerializedName("watch_work")
    private final Boolean watchWork;
    
    
    @Expose @SerializedName("subtype")
    private final String subtype;
    
    
    /**
     * @param block the block data to publish
     * @param force (optional) whether fork resolution should be forced
     */
    public RequestProcess(Block block, Boolean force) {
        this(block.getJsonString(), force, null, null);
    }
    
    /**
     * @param blockJson the block's JSON contents
     * @param force     (optional) whether fork resolution should be forced
     * @param subtype   (optional) the subtype of the block
     */
    public RequestProcess(String blockJson, Boolean force, String subtype) {
        this(blockJson, force, null, subtype);
    }
    
    /**
     * @param blockJson the block's JSON contents
     * @param force     (optional) whether fork resolution should be forced
     * @param watchWork (optional) whether the block should be placed on watch for confirmation
     */
    public RequestProcess(String blockJson, Boolean force, Boolean watchWork) {
        this(blockJson, force, watchWork, null);
    }
    
    /**
     * @param blockJson the block's JSON contents
     * @param force     (optional) whether fork resolution should be forced
     * @param watchWork (optional) whether the block should be placed on watch for confirmation
     * @param subtype   (optional) the subtype of the block
     */
    public RequestProcess(String blockJson, Boolean force, Boolean watchWork, String subtype) {
        super("process", ResponseBlockHash.class);
        this.blockJson = blockJson;
        this.force = force;
        this.subtype = subtype;
        this.watchWork = watchWork;
    }
    
    
    /**
     * @return the requested block's JSON contents
     */
    public String getBlockJson() {
        return blockJson;
    }
    
    /**
     * @return whether fork resolution should be forced
     */
    public boolean shouldForce() {
        return force;
    }
    
    /**
     * @return the requested block's subtype
     */
    public String getSubtype() {
        return subtype;
    }
    
    /**
     * @return whether the block should be placed on watch for confirmation
     */
    public Boolean getWatchWork() {
        return watchWork;
    }
    
}
