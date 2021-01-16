/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.websocket.topic.TopicConfirmation;

import java.time.Instant;

public class TopicMessageConfirmation {
    
    @Expose @SerializedName("account")
    private NanoAccount account;
    
    @Expose @SerializedName("amount")
    private NanoAmount amount;
    
    @Expose @SerializedName("hash")
    private HexData hash;
    
    @Expose @SerializedName("confirmation_type")
    private ConfirmationType confirmationType;
    
    @Expose @SerializedName("election_info")
    private ElectionInfo electionInfo;
    
    @Expose @SerializedName("block")
    private Block block;
    
    
    /**
     * Returns the account which the block belongs to.
     * @return the account which the block belongs to
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * Returns the amount of the transaction.
     * @return the amount of the transaction
     */
    public NanoAmount getAmount() {
        return amount;
    }
    
    /**
     * Returns the 64-character hexadecimal hash of the block.
     * @return the block's hash
     */
    public HexData getHash() {
        return hash;
    }
    
    /**
     * Returns the type of confirmation.
     * @return the type of confirmation
     */
    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }
    
    /**
     * Returns an object containing additional election information.
     * <p>Note that this needs to be enabled in the subscribe arguments using
     * {@link TopicConfirmation.SubArgs#includeElectionInfo()} ()}, otherwise it will return null.</p>
     * @return additional election information
     */
    public ElectionInfo getElectionInfo() {
        return electionInfo;
    }
    
    /**
     * Returns the blocks full contents.
     * <p>Note that this needs to be enabled in the subscribe arguments using
     * {@link TopicConfirmation.SubArgs#includeBlockContents()}, otherwise it will return null.</p>
     * @return the block, or null if not enabled
     */
    public Block getBlock() {
        return block;
    }
    
    
    public enum ConfirmationType {
        ACTIVE_QUORUM,
        ACTIVE_CONFIRMATION_HEIGHT,
        INACTIVE;
    }
    
    public static class ElectionInfo {
        @Expose @SerializedName("duration")
        private int duration;
    
        @Expose @SerializedName("time")
        private Instant timestamp;
    
        @Expose @SerializedName("tally")
        private NanoAmount tally;
    
        @Expose @SerializedName("request_count")
        private int requestCount;
    
        @Expose @SerializedName("blocks")
        private int blocks;
    
        @Expose @SerializedName("voters")
        private int voters;
    
    
        /**
         * Returns the total duration of the election, in milliseconds
         * @return the duration of the election, in milliseconds
         */
        public int getDuration() {
            return duration;
        }
    
        /**
         * Returns the timestamp of when the election ended.
         * @return the timestamp of when the election ended
         */
        public Instant getTimestamp() {
            return timestamp;
        }
    
        /**
         * The total amount of delegated Nano which was used to vote in this election.
         * @return the total voting tally
         */
        public NanoAmount getTally() {
            return tally;
        }
        
        // TODO: Javadoc explaining what this is
        public int getRequestCount() {
            return requestCount;
        }
    
        /**
         * Returns the number of blocks included in this election.
         * @return the number of blocks included in this election
         */
        public int getBlocks() {
            return blocks;
        }
    
        /**
         * Returns the number of representatives which voted in this election.
         * @return the number of representatives which voted in this election
         */
        public int getVoters() {
            return voters;
        }
    }
    
}
