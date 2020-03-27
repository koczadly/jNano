package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a {@code receive} block, and the associated data.
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class ReceiveBlock extends Block {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    
    ReceiveBlock() {
        super(BlockType.RECEIVE);
    }
    
    public ReceiveBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution,
                        String previousBlockHash, String sourceBlockHash) {
        super(BlockType.RECEIVE, hash, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.sourceBlockHash = sourceBlockHash;
    }
    
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
}
