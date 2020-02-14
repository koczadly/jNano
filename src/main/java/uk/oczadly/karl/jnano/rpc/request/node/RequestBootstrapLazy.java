package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to initialize bootstrap from a given block hash.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code bootstrap_lazy}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_lazy">Official RPC documentation</a>
 */
public class RequestBootstrapLazy extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("force")
    private final Boolean force;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestBootstrapLazy(String blockHash) {
        this(blockHash, null);
    }
    
    /**
     * @param blockHash the block's hash
     * @param force     (optional) whether all current bootstraps should be forcefully closed
     */
    public RequestBootstrapLazy(String blockHash, Boolean force) {
        super("bootstrap_lazy", ResponseSuccessful.class);
        this.blockHash = blockHash;
        this.force = force;
    }
    
    
    /**
     * @return the requested block hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return whether all current bootstraps should be forcefully closed
     */
    public Boolean getForce() {
        return force;
    }
    
}
