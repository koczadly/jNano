/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.Block;

import java.math.BigInteger;
import java.time.Instant;

public class TopicMessageConfirmation {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("amount")
    private BigInteger amount;
    
    @Expose @SerializedName("hash")
    private String hash;
    
    @Expose @SerializedName("confirmation_type")
    private ConfirmationType confirmationType;
    
    @Expose @SerializedName("election_info")
    private ElectionInfo electionInfo;
    
    @Expose @SerializedName("block")
    private Block block;
    
    
    public String getAccount() {
        return account;
    }
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public String getHash() {
        return hash;
    }
    
    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }
    
    public ElectionInfo getElectionInfo() {
        return electionInfo;
    }
    
    public Block getBlock() {
        return block;
    }
    
    
    
    public enum ConfirmationType {
        @SerializedName("active_quorum")                ACTIVE_QUORUM,
        @SerializedName("active_confirmation_height")   ACTIVE_CONFIRMATION_HEIGHT,
        @SerializedName("inactive")                     INACTIVE;
    }
    
    public static class ElectionInfo {
        @Expose @SerializedName("duration")
        private int duration;
    
        @Expose @SerializedName("time")
        private Instant timestamp;
    
        @Expose @SerializedName("tally")
        private BigInteger tally;
    
        @Expose @SerializedName("request_count")
        private int requestCount;
    
        @Expose @SerializedName("blocks")
        private int blocks;
    
        @Expose @SerializedName("voters")
        private int voters;
    
    
        public int getDuration() {
            return duration;
        }
    
        public Instant getTimestamp() {
            return timestamp;
        }
    
        public BigInteger getTally() {
            return tally;
        }
    
        public int getRequestCount() {
            return requestCount;
        }
    
        public int getBlocks() {
            return blocks;
        }
    
        public int getVoters() {
            return voters;
        }
    }
    
}
