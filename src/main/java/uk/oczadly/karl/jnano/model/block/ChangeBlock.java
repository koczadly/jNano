package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a {@code change} block, and the associated data.
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class ChangeBlock extends Block {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private String representativeAccount;
    
    
    ChangeBlock() {
        super(BlockType.CHANGE);
    }
    
    public ChangeBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution,
                       String previousBlockHash, String representativeAccount) {
        super(BlockType.CHANGE, hash, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public final String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
