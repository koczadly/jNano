/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.math.BigInteger;
import java.util.Map;

/**
 * This response class contains information about a specific election.
 */
public class ResponseConfirmationInfo extends RpcResponse {
    
    @Expose @SerializedName("announcements")
    private int announcements;
    
    @Expose @SerializedName("last_winner")
    private String lastWinner;
    
    @Expose @SerializedName("total_tally")
    private BigInteger totalTally;
    
    @Expose @SerializedName("blocks")
    private Map<String, BlockConfirmation> blocks;
    
    
    /**
     * @return the number of announcements
     */
    public int getAnnouncements() {
        return announcements;
    }
    
    /**
     * @return the last winning block hash
     */
    public String getLastWinner() {
        return lastWinner;
    }
    
    /**
     * @return the total voting tally this election
     */
    public BigInteger getTotalTally() {
        return totalTally;
    }
    
    /**
     * Map follows the structure {@code block hash -> confirmation info}.
     *
     * @return a map of confirmations.
     */
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
        
        
        /**
         * @return the total vote
         */
        public BigInteger getTally() {
            return tally;
        }
        
        /**
         * @return the contents of the block
         */
        public Block getBlockContents() {
            return blockContents;
        }
        
        /**
         * Map follows the structure {@code representative address -> voting weight (in RAW)}.
         *
         * @return a map of representatives who voted on this block
         */
        public Map<String, BigInteger> getRepresentatives() {
            return representatives;
        }
    }
    
}
