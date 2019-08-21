package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.util.List;

public class ResponseAccountHistory extends RpcResponse {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("history")
    private List<Block> history;
    
    @Expose @SerializedName("previousHash")
    private String previousHash;
    
    @Expose @SerializedName("nextHash")
    private String nextHash;
    
    
    
    public String getAccount() {
        return account;
    }
    
    public List<Block> getHistory() {
        return history;
    }
    
    public String getPreviousBlockHash() {
        return previousHash;
    }
    
    public String getNextBlockHash() {
        return nextHash;
    }
    
}
