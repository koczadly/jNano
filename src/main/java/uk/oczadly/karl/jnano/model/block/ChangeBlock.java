package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeBlock extends Block {
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    ChangeBlock() {
        super(BlockType.CHANGE);
    }
    
    public ChangeBlock(String jsonRepresentation, String signature, String workSolution, String previousBlockHash, String representativeAccount) {
        super(BlockType.CHANGE, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.representativeAccount = representativeAccount;
    }
    
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public final String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
