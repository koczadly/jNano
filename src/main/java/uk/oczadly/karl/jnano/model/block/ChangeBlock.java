package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;

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
    
    public ChangeBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution,
                       String previousBlockHash, NanoAccount representativeAccount) {
        super(BlockType.CHANGE, hash, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
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
