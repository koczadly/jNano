package in.bigdolph.jnano.rpc.query.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

public class ReceiveBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    
    public ReceiveBlockCreateRequest(String privateKey, String workSolution, String sourceBlockHash, String previousBlockHash) {
        super(BlockType.RECEIVE, privateKey, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.previousBlockHash = previousBlockHash;
    }
    
    public ReceiveBlockCreateRequest(String walletId, String account, String workSolution, String sourceBlockHash, String previousBlockHash) {
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
