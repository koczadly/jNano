package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
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
    private final String rootHash;
    
    @Expose @SerializedName("use_peers")
    private final Boolean usePeers;
    
    @Expose @SerializedName("difficulty")
    private final WorkDifficulty difficulty;
    
    @Expose @SerializedName("multiplier")
    private final Double multiplier;
    
    @Expose @SerializedName("block")
    private final Block block;
    
    
    /**
     * @param root the source root
     * @deprecated Use nested builder class
     * @see Builder#Builder(String)
     */
    @Deprecated(forRemoval = true)
    public RequestWorkGenerate(String root) {
        this(root, null, null, null, null);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty value.
     *
     * @param root       the source root
     * @param usePeers   (optional) whether work peers should be used
     * @param difficulty (optional) the absolute difficulty value
     * @deprecated Use nested builder class
     * @see Builder#Builder(String)
     */
    @Deprecated(forRemoval = true)
    public RequestWorkGenerate(String root, Boolean usePeers, WorkDifficulty difficulty) {
        this(root, null, difficulty, null, usePeers);
    }
    
    /**
     * Constructs a work generation request with a specified difficulty multiplier.
     *
     * @param root       the source root
     * @param usePeers   (optional) whether work peers should be used
     * @param multiplier (optional) the difficulty multiplier
     * @deprecated Use nested builder class
     * @see Builder#Builder(String)
     */
    @Deprecated(forRemoval = true)
    public RequestWorkGenerate(String root, Boolean usePeers, Double multiplier) {
        this(root, null, null, multiplier, usePeers);
    }
    
    
    private RequestWorkGenerate(String blockHash, Block block, WorkDifficulty difficulty,
                                Double multiplier, Boolean usePeers) {
        super("work_generate", ResponseWork.class);
        this.rootHash = blockHash;
        this.block = block;
        this.usePeers = usePeers;
        this.difficulty = difficulty;
        this.multiplier = multiplier;
    }
    
    
    /**
     * @return the block's hash
     * @deprecated Use {@link #getRootHash()} ()}
     */
    @Deprecated(forRemoval = true)
    public String getBlockHash() {
        return rootHash;
    }
    
    /**
     * @return the work source root
     */
    public String getRootHash() {
        return rootHash;
    }
    
    /**
     * @return the block to generate work for
     */
    public Block getBlock() {
        return block;
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
    
    
    
    public static final class Builder {
        private String root;
        private Block block;
        private WorkDifficulty diff;
        private Double diffMult;
        private Boolean usePeers;
    
        /**
         * Generates work for the next block, from a given root hash.
         * @param root the root of the work (previous block hash, or account public key for open blocks)
         */
        public Builder(String root) {
            this(root, null);
        }
    
        /**
         * Generates work for the supplied block. The difficulty threshold will be automatically calculated based on the
         * block type and current network conditions.
         * @param block the block to generate work for
         * @param <B>   the block interfaces
         */
        public <B extends Block & IBlockPrevious & IBlockAccount> Builder(B block) {
            this((block.getPreviousBlockHash() == null || block.getPreviousBlockHash().equals(JNanoHelper.ZEROES_64))
                    ? block.getAccount().toPublicKey() : block.getPreviousBlockHash(), block);
        }
    
        /**
         * Generates work for the supplied block, using the given root hash. The difficulty threshold will be
         * automatically calculated based on the block type and current network conditions.
         * @param root  the root of the work (previous block hash, or account public key for open blocks)
         * @param block the block to generate work for
         */
        public Builder(String root, Block block) {
            if (!JNanoHelper.isValidHex(root, 64))
                throw new IllegalArgumentException("Root string is invalid.");
            if (root == null)
                throw new IllegalArgumentException("Root cannot be null.");
            this.root = root;
            this.block = block;
        }
    
    
        public Builder setDifficulty(WorkDifficulty diff) {
            this.diff = diff;
            this.diffMult = null;
            return this;
        }
    
        public Builder setDifficultyMultiplier(double diffMult) {
            this.diffMult = diffMult;
            this.diff = null;
            return this;
        }
    
        public Builder setUsePeers(boolean usePeers) {
            this.usePeers = usePeers;
            return this;
        }
        
        
        public RequestWorkGenerate build() {
            return new RequestWorkGenerate(root, block, diff, diffMult, usePeers);
        }
    }
    
}
