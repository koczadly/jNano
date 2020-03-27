package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

import java.util.List;

/**
 * This response class contains historical information about an account.
 */
public class ResponseAccountHistory extends RpcResponse {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("history")
    private List<Block> history;
    
    @Expose @SerializedName("previous")
    private String previousHash;
    
    @Expose @SerializedName("next")
    private String nextHash;
    
    
    /**
     * @return the account's address
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return a list of blocks within this account
     */
    public List<Block> getHistory() {
        return history;
    }
    
    /**
     * @return the previous block hash, or null if traversing backwards
     *
     * @see #getNextBlockHash()
     */
    public String getPreviousBlockHash() {
        return previousHash;
    }
    
    /**
     * @return the next block hash, or null if traversing forwards
     *
     * @see #getPreviousBlockHash()
     */
    public String getNextBlockHash() {
        return nextHash;
    }
    
}
