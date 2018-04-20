package uk.oczadly.karl.jnano.rpc.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;

public class OpenBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    public OpenBlockCreateRequest(String sourceBlockHash, String representativeAccount, String privateKey, String workSolution) {
        super(BlockType.OPEN, privateKey, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    public OpenBlockCreateRequest(String sourceBlockHash, String representativeAccount, String walletId, String account, String workSolution) {
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
