package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * Represents an {@code open} block, and the associated data.
 *
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class OpenBlock extends Block {
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose @SerializedName("account")
    private NanoAccount accountAddress;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAccount;
    
    
    OpenBlock() {
        super(BlockType.OPEN);
    }
    
    public OpenBlock(String signature, WorkSolution work, String sourceBlockHash, NanoAccount accountAddress,
                     NanoAccount representativeAccount) {
        this(null, null, signature, work, sourceBlockHash, accountAddress, representativeAccount);
    }
    
    protected OpenBlock(JsonObject jsonRepresentation, String hash, String signature, WorkSolution work,
                     String sourceBlockHash, NanoAccount accountAddress, NanoAccount representativeAccount) {
        super(BlockType.OPEN, hash, jsonRepresentation, signature, work);
    
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNanoHelper.isValidHex(sourceBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (representativeAccount == null) throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.sourceBlockHash = sourceBlockHash;
        this.accountAddress = accountAddress;
        this.representativeAccount = representativeAccount;
    }
    
    
    /**
     * @return the hash of the corresponding send block
     */
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    /**
     * @return the account which this block belongs to
     */
    public final NanoAccount getAccountAddress() {
        return accountAddress;
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
                JNanoHelper.ENCODER_HEX.decode(getSourceBlockHash()),
                getRepresentativeAccount().getPublicKeyBytes(),
                getAccountAddress().getPublicKeyBytes()
        };
    }
    
}
