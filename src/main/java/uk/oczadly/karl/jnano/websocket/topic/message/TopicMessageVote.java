/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicMessageVote {
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("signature")
    private String signature;
    
    @Expose @SerializedName("sequence")
    private long sequence;
    
    @Expose @SerializedName("blocks")
    private List<String> blocks;
    
    @Expose @SerializedName("type")
    private Type type;
    
    
    public String getAccount() {
        return account;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public long getSequence() {
        return sequence;
    }
    
    public List<String> getBlockHashes() {
        return blocks;
    }
    
    public Type getType() {
        return type;
    }
    
    
    
    public enum Type {
        @SerializedName("vote")          VOTE,
        @SerializedName("replay")        REPLAY,
        @SerializedName("indeterminate") INDETERMINATE
    }
    
}
