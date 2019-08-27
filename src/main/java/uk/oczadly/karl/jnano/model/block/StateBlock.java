package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

//TODO may need confirming
public class StateBlock extends Block {
    
    @Expose @SerializedName("account")
    private String accountAddress;
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private String representativeAddress;
    
    @Expose @SerializedName("balance")
    private BigInteger newBalance;
    
    @Expose @SerializedName("link")
    private String linkData;
    
    @Expose @SerializedName("link_as_account")
    private String linkAccount;
    
    
    StateBlock() {
        super(BlockType.STATE);
    }
    
    public StateBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution, String accountAddress, String previousBlockHash, String representativeAddress, BigInteger newBalance, String linkData, String linkAccount) {
        super(BlockType.STATE, hash, jsonRepresentation, signature, workSolution);
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.newBalance = newBalance;
        this.linkData = linkData;
        this.linkAccount = linkAccount;
    }
    
    
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public String getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public final BigInteger getNewBalance() {
        return newBalance;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    public String getLinkAccount() {
        return linkAccount;
    }
    
}
