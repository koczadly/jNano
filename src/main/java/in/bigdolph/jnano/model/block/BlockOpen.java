package in.bigdolph.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class BlockOpen extends Block {
    
    @Expose
    @SerializedName("source")
    private String sourceBlockHash;
    
    @Expose
    @SerializedName("account")
    private String accountAddress;
    
    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    BlockOpen() {
        super(BlockType.OPEN);
    }
    
    public BlockOpen(String signature, String workSolution, String sourceBlockHash, String accountAddress, String representativeAccount) {
        super(BlockType.OPEN, signature, workSolution);
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
