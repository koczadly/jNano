package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class SendBlock extends Block {
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("destination")
    private String destinationAccount;
    
    @Expose @SerializedName("balance")
    private BigInteger newBalance;
    
    
    SendBlock() {
        super(BlockType.SEND);
    }
    
    public SendBlock(JsonObject jsonRepresentation, String hash, String signature, String workSolution, String previousBlockHash, String destinationAccount, BigInteger newBalance) {
        super(BlockType.SEND, hash, jsonRepresentation, signature, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.destinationAccount = destinationAccount;
        this.newBalance = newBalance;
    }
    
    
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public final String getDestinationAccount() {
        return destinationAccount;
    }
    
    public final BigInteger getNewBalance() {
        return newBalance;
    }
    
}
