package in.bigdolph.jnano.rpc.query.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

public class ChangeBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    
    public ChangeBlockCreateRequest(String workSolution, String privateKey, String representativeAccount, String previousBlockHash) {
        super(BlockType.CHANGE, workSolution, privateKey);
        this.representativeAccount = representativeAccount;
        this.previousBlockHash = previousBlockHash;
    }
    
    public ChangeBlockCreateRequest(String workSolution, String walletId, String account, String representativeAccount, String previousBlockHash) {
        super(BlockType.CHANGE, workSolution, walletId, account);
        this.representativeAccount = representativeAccount;
        this.previousBlockHash = previousBlockHash;
    }
    
    
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
}
