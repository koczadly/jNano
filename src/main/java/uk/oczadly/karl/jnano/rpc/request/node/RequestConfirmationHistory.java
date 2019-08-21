package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationHistory;

public class RequestConfirmationHistory extends RpcRequest<ResponseConfirmationHistory> {
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    
    public RequestConfirmationHistory() {
        this(null);
    }
    
    public RequestConfirmationHistory(String blockHash) {
        super("confirmation_history", ResponseConfirmationHistory.class);
        this.blockHash = blockHash;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
