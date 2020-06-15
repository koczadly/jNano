package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;

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
    
    public OpenBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution,
                     String sourceBlockHash, NanoAccount accountAddress, NanoAccount representativeAccount) {
        super(BlockType.OPEN, hash, jsonRepresentation, signature, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.accountAddress = accountAddress;
        this.representativeAccount = representativeAccount;
    }
    
    
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    public final NanoAccount getAccountAddress() {
        return accountAddress;
    }
    
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
