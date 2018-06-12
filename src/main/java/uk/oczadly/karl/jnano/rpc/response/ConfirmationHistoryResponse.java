package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.math.BigInteger;
import java.util.List;

public class ConfirmationHistoryResponse extends RpcResponse {
    
    @Expose
    @SerializedName("confirmations")
    private List<Confirmation> confirmationHistory;
    
    
    
    public List<Confirmation> getHistory() {
        return confirmationHistory;
    }
    
    
    
    public static class Confirmation {
        
        @Expose
        @SerializedName("hash")
        private String hash;
    
        @Expose
        @SerializedName("tally")
        private BigInteger tally;
    
        
        public String getHash() {
            return hash;
        }
    
        public BigInteger getTally() {
            return tally;
        }
        
    }
    
}
