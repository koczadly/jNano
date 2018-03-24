package in.bigdolph.jnano.rpc.query.request.compute.blockcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.request.compute.blockcreate.BlockCreateRequest;

public class OpenBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    public OpenBlockCreateRequest(String workSolution, String privateKey, String sourceBlockHash, String representativeAccount) {
        super(BlockType.OPEN, workSolution, privateKey);
        this.sourceBlockHash = sourceBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    public OpenBlockCreateRequest(String workSolution, String walletId, String account, String sourceBlockHash, String representativeAccount) {
        super(BlockType.OPEN, workSolution, walletId, account);
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
