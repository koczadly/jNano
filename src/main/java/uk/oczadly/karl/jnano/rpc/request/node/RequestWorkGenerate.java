package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

/**
 * This request class is used to generate work for the specified block hash.
 * <br>Calls the RPC command {@code work_generate}, and returns a {@link ResponseWork} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_generate">Official RPC documentation</a>
 */
public class RequestWorkGenerate extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("json_block")
    private final boolean jsonBlock = true;
    
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    @Expose @SerializedName("use_peers")
    private final Boolean usePeers;
    
    @Expose @SerializedName("difficulty")
    private final WorkDifficulty difficulty;
    
    @Expose @SerializedName("multiplier")
    private final Double multiplier;
    
    @Expose @SerializedName("block")
    private final Block block;
    
    
    /**
     * @param blockHash the block's hash
     */
    public RequestWorkGenerate(String blockHash) {
        this(blockHash, null, null, null, null);
    }
    
    /**
     * @param block the block to generate work for
     */
    public RequestWorkGenerate(Block block) {
        this(null, null, null, null, block);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty value.
     *
     * @param blockHash  the block's hash
     * @param usePeers   (optional) whether work peers should be used
     * @param difficulty (optional) the absolute difficulty value
     */
    public RequestWorkGenerate(String blockHash, Boolean usePeers, WorkDifficulty difficulty) {
        this(blockHash, usePeers, difficulty, null, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty value.
     *
     * @param block      the block to generate work for
     * @param usePeers   (optional) whether work peers should be used
     * @param difficulty (optional) the absolute difficulty value
     */
    public RequestWorkGenerate(Block block, Boolean usePeers, WorkDifficulty difficulty) {
        this(null, usePeers, difficulty, null, block);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty multiplier.
     *
     * @param blockHash  the block's hash
     * @param usePeers   (optional) whether work peers should be used
     * @param multiplier (optional) the difficulty multiplier
     */
    public RequestWorkGenerate(String blockHash, Boolean usePeers, Double multiplier) {
        this(blockHash, usePeers, null, multiplier, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty multiplier.
     *
     * @param block      the block to generate work for
     * @param usePeers   (optional) whether work peers should be used
     * @param multiplier (optional) the difficulty multiplier
     */
    public RequestWorkGenerate(Block block, Boolean usePeers, Double multiplier) {
        this(null, usePeers, null, multiplier, block);
    }
    
    private RequestWorkGenerate(String blockHash, Boolean usePeers, WorkDifficulty difficulty, Double multiplier,
                                Block block) {
        super("work_generate", ResponseWork.class);
        this.blockHash = (blockHash == null && block != null) ? block.getHash() : blockHash;
        this.usePeers = usePeers;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
        this.block = block;
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
    public WorkDifficulty getDifficulty() {
        return difficulty;
    }
    
    /**
     * @return the request difficulty multiplier
     */
    public Double getMultiplier() {
        return multiplier;
    }
    
    /**
     * @return the block to generate work for
     */
    public Block getBlock() {
        return block;
    }
    
}
