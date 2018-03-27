package in.bigdolph.jnano.rpc.query.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

public class OpenBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    public OpenBlockCreateRequest(String privateKey, String workSolution, String sourceBlockHash, String representativeAccount) {
        super(BlockType.OPEN, privateKey, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    public OpenBlockCreateRequest(String walletId, String account, String workSolution, String sourceBlockHash, String representativeAccount) {
        super(BlockType.OPEN, walletId, account, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    
    public String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
