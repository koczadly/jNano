package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

/**
 * This request class is used to generate work for the specified block hash.
 * The server responds with a {@link ResponseWork} data object.<br>
 * Calls the internal RPC method {@code work_generate}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_generate">Official RPC documentation</a>
 */
public class RequestWorkGenerate extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("use_peers")
    private final Boolean usePeers;
    
    @Expose @SerializedName("difficulty")
    private final String difficulty;
    
    @Expose @SerializedName("multiplier")
    private final Double multiplier;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestWorkGenerate(String blockHash) {
        this(blockHash, null, null, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty value.
     * @param blockHash     the block's hash
     * @param usePeers      (optional) whether work peers should be used
     * @param difficulty    (optional) the absolute difficulty value
     */
    public RequestWorkGenerate(String blockHash, Boolean usePeers, String difficulty) {
        this(blockHash, usePeers, difficulty, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty multiplier.
     * @param blockHash     the block's hash
     * @param usePeers      (optional) whether work peers should be used
     * @param multiplier    (optional) the difficulty multiplier
     */
    public RequestWorkGenerate(String blockHash, Boolean usePeers, Double multiplier) {
        this(blockHash, usePeers, null, multiplier);
    }
    
    private RequestWorkGenerate(String blockHash, Boolean usePeers, String difficulty, Double multiplier) {
        super("work_generate", ResponseWork.class);
        this.blockHash = blockHash;
        this.usePeers = usePeers;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    /**
     * @return the block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
    /**
     * @return whether remote work peers should be used to generate the work
     */
    public boolean getUsePeers() {
        return usePeers;
    }
    
    /**
     * @return the request difficulty value
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the request difficulty multiplier
     */
    public Double getMultiplier() {
        return multiplier;
    }
    
}
