package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * Represents a {@code receive} block, and the associated data.
 */
public class ReceiveBlock extends Block implements IBlockPrevious, IBlockSource {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("source")
    private String sourceBlockHash;
    
    
    ReceiveBlock() {
        super(BlockType.RECEIVE);
    }
    
    public ReceiveBlock(String signature, WorkSolution work, String previousBlockHash, String sourceBlockHash) {
        this(null, signature, work, previousBlockHash, sourceBlockHash);
    }
    
    protected ReceiveBlock(String hash, String signature, WorkSolution work,
                        String previousBlockHash, String sourceBlockHash) {
        super(BlockType.RECEIVE, hash, signature, work);
    
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (sourceBlockHash == null) throw new IllegalArgumentException("Source block hash cannot be null.");
        if (!JNH.isValidHex(sourceBlockHash, 64))
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
                JNH.ENC_16.decode(getPreviousBlockHash()),
                JNH.ENC_16.decode(getSourceBlockHash())
        };
    }
    
}
