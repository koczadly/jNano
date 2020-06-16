package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * Represents a {@code change} block, and the associated data.
 *
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class ChangeBlock extends Block {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAccount;
    
    
    ChangeBlock() {
        super(BlockType.CHANGE);
    }
    
    public ChangeBlock(String signature, WorkSolution workSolution, String previousBlockHash,
                       NanoAccount representativeAccount) {
        this(null, null, signature, workSolution, previousBlockHash, representativeAccount);
    }
    
    protected ChangeBlock(JsonObject jsonRepresentation, String hash, String signature, WorkSolution workSolution,
                       String previousBlockHash, NanoAccount representativeAccount) {
        super(BlockType.CHANGE, hash, jsonRepresentation, signature, workSolution);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNanoHelper.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (representativeAccount == null) throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.previousBlockHash = previousBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    /**
     * @return the previous block hash in this account's blockchain
     */
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    /**
     * @return the representative address for this account
     */
    public final NanoAccount getRepresentativeAccount() {
        return representativeAccount;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNanoHelper.ENCODER_HEX.decode(getPreviousBlockHash()),
                getRepresentativeAccount().getPublicKeyBytes()
        };
    }
    
}
