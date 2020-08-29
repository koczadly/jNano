/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic.options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.Arrays;
import java.util.List;

public class TopicOptionsVote {
    
    @Expose @SerializedName("representatives")
    private List<NanoAccount> representatives;
    
    @Expose @SerializedName("include_replays")
    private Boolean includeReplays;
    
    @Expose @SerializedName("include_indeterminate")
    private Boolean includeIndeterminate;
    
    
    public List<NanoAccount> getRepresentatives() {
        return representatives;
    }
    
    public TopicOptionsVote setRepresentatives(List<NanoAccount> representatives) {
        this.representatives = representatives;
        return this;
    }
    
    public TopicOptionsVote setRepresentatives(NanoAccount... representatives) {
        return setRepresentatives(Arrays.asList(representatives));
    }
    
    public Boolean getIncludeReplays() {
        return includeReplays;
    }
    
    public TopicOptionsVote setIncludeReplays(Boolean includeReplays) {
        this.includeReplays = includeReplays;
        return this;
    }
    
    public Boolean getIncludeIndeterminate() {
        return includeIndeterminate;
    }
    
    public TopicOptionsVote setIncludeIndeterminate(Boolean includeIndeterminate) {
        this.includeIndeterminate = includeIndeterminate;
        return this;
    }
    
}
