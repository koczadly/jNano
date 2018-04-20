package uk.oczadly.karl.jnano.rpc.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;

public class ReceiveBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    
    public ReceiveBlockCreateRequest(String sourceBlockHash, String previousBlockHash, String privateKey, String workSolution) {
        super(BlockType.RECEIVE, privateKey, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.previousBlockHash = previousBlockHash;
    }
    
    public ReceiveBlockCreateRequest(String sourceBlockHash, String previousBlockHash, String walletId, String account, String workSolution) {
        super(BlockType.RECEIVE, walletId, account, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.previousBlockHash = previousBlockHash;
    }
    
    
    
    public String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
}
