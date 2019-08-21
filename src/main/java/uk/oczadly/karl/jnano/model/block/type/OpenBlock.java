package uk.oczadly.karl.jnano.model.block.type;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.BlockType;

public class OpenBlock extends Block {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("account")
    private String accountAddress;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    OpenBlock() {
        super(BlockType.OPEN);
    }
    
    public OpenBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution, String sourceBlockHash, String accountAddress, String representativeAccount) {
        super(BlockType.OPEN, hash, jsonRepresentation, signature, workSolution);
        this.sourceBlockHash = sourceBlockHash;
        this.accountAddress = accountAddress;
        this.representativeAccount = representativeAccount;
    }
    
    
    public final String getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    public final String getAccountAddress() {
        return accountAddress;
    }
    
    public final String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
