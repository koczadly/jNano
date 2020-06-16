package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * Represents a {@code receive} block, and the associated data.
 *
 * @deprecated Only state blocks ({@link StateBlock}) are supported by the Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class ReceiveBlock extends Block implements BlockInterfaces.Previous, BlockInterfaces.Source {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    
    ReceiveBlock() {
        super(BlockType.RECEIVE);
    }
    
    public ReceiveBlock(String signature, WorkSolution work, String previousBlockHash, String sourceBlockHash) {
        this(null, null, signature, work, previousBlockHash, sourceBlockHash);
    }
    
    protected ReceiveBlock(JsonObject jsonRepresentation, String hash, String signature, WorkSolution work,
                        String previousBlockHash, String sourceBlockHash) {
        super(BlockType.RECEIVE, hash, jsonRepresentation, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNanoHelper.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNanoHelper.isValidHex(sourceBlockHash, 64))
            throw new IllegalArgumentException("Source block hash is invalid.");
        
        this.previousBlockHash = previousBlockHash;
        this.sourceBlockHash = sourceBlockHash;
    }
    
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                JNanoHelper.ENCODER_HEX.decode(getPreviousBlockHash()),
                JNanoHelper.ENCODER_HEX.decode(getSourceBlockHash())
        };
    }
    
}
