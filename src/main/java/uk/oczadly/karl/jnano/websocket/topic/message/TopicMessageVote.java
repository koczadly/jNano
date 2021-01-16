/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.gsonadapters.UnsignedLongAdapter;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.List;

public class TopicMessageVote {
    
    @Expose @SerializedName("account")
    private NanoAccount account;
    
    @Expose @SerializedName("signature")
    private HexData signature;
    
    @Expose @SerializedName("sequence")
    @JsonAdapter(UnsignedLongAdapter.class)
    private long sequence;
    
    @Expose @SerializedName("blocks")
    private List<String> blocks;
    
    @Expose @SerializedName("type")
    private Type type;
    
    
    /**
     * Returns the representative account which voted for this election.
     * @return the representative account which voted for this election
     */
    public NanoAccount getAccount() {
        return account;
    }
    
    /**
     * Returns the verification signature signed by the representative.
     * @return the the signature signed by the representative
     */
    public HexData getSignature() {
        return signature;
    }
    
    /**
     * Returns the current vote sequence of the representative.
     * @return the vote sequence
     */
    public long getSequence() {
        return sequence;
    }
    
    /**
     * Returns a list of block hashes which the representative is voting for.
     * @return a list of block hashes being voted for
     */
    public List<String> getBlockHashes() {
        return blocks;
    }
    
    /**
     * Returns the type of vote.
     * @return the type of vote
     */
    public Type getType() {
        return type;
    }
    
    
    public enum Type {
        /** The first time the vote has been seen. */
        VOTE,
        /** The vote has been seen before. */
        REPLAY,
        /** Could not be determined due to lack of an associated election. */
        INDETERMINATE
    }
    
}
