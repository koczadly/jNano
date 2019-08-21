package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.math.BigInteger;
import java.util.Map;


public class ResponseConfirmationInfo extends RpcResponse {

    @Expose @SerializedName("announcements")
    private int announcements;
    
    @Expose @SerializedName("last_winner")
    private String lastWinner;
    
    @Expose @SerializedName("total_tally")
    private BigInteger totalTally;
    
    @Expose @SerializedName("blocks")
    private Map<String, BlockConfirmation> blocks;
    
    
    public int getAnnouncements() {
        return announcements;
    }
    
    public String getLastWinner() {
        return lastWinner;
    }
    
    public BigInteger getTotalTally() {
        return totalTally;
    }
    
    public Map<String, BlockConfirmation> getBlocks() {
        return blocks;
    }
    
    
    
    public static class BlockConfirmation {
        @Expose @SerializedName("tally")
        private BigInteger tally;
    
        @Expose @SerializedName("contents")
        private Block blockContents;
    
        @Expose @SerializedName("representatives")
        private Map<String, BigInteger> representatives;
    
    
        public BigInteger getTally() {
            return tally;
        }
    
        public Block getBlockContents() {
            return blockContents;
        }
    
        public Map<String, BigInteger> getRepresentatives() {
            return representatives;
        }
    }
    
}
