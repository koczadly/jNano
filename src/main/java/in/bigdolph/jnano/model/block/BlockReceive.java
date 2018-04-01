package in.bigdolph.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class BlockReceive extends Block {
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    
    BlockReceive() {
        super(BlockType.RECEIVE);
    }
    
    public BlockReceive(String jsonRepresentation, String signature, String workSolution, String previousBlockHash, String sourceBlockHash) {
        super(BlockType.RECEIVE, jsonRepresentation, signature, workSolution);
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
