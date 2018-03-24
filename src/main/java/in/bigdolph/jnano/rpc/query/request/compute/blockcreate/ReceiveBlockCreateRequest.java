package in.bigdolph.jnano.rpc.query.request.compute.blockcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.request.compute.blockcreate.BlockCreateRequest;

public class ReceiveBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    
    public ReceiveBlockCreateRequest(String workSolution, String privateKey, String sourceBlockHash, String previousBlockHash) {
        super(BlockType.RECEIVE, workSolution, privateKey);
        this.sourceBlockHash = sourceBlockHash;
        this.previousBlockHash = previousBlockHash;
    }
    
    public ReceiveBlockCreateRequest(String workSolution, String walletId, String account, String sourceBlockHash, String previousBlockHash) {
        super(BlockType.RECEIVE, workSolution, walletId, account);
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
