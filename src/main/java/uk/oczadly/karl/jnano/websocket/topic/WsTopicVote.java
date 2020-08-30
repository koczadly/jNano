/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.TopicWithSubscribeParams;
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageVote;

import java.util.Arrays;
import java.util.List;

public class WsTopicVote extends WsTopic<TopicMessageVote>
        implements TopicWithSubscribeParams<WsTopicVote.Parameters> {
    
    public WsTopicVote(NanoWebSocketClient client) {
        super("vote", client, TopicMessageVote.class);
    }
    
    
    @Override
    public void subscribe(Parameters params) {
        _subscribe(params);
    }
    
    @Override
    public boolean subscribeBlocking(long timeout, Parameters params) throws InterruptedException {
        return _subscribeBlocking(timeout, params);
    }
    
    
    
    public static final class Parameters {
        @Expose @SerializedName("representatives")
        private List<NanoAccount> representatives;
        
        @Expose @SerializedName("include_replays")
        private Boolean includeReplays;
        
        @Expose @SerializedName("include_indeterminate")
        private Boolean includeIndeterminate;
        
        
        public List<NanoAccount> getRepresentatives() {
            return representatives;
        }
        
        public Parameters setRepresentatives(List<NanoAccount> representatives) {
            this.representatives = representatives;
            return this;
        }
        
        public Parameters setRepresentatives(NanoAccount... representatives) {
            return setRepresentatives(Arrays.asList(representatives));
        }
        
        public Boolean getIncludeReplays() {
            return includeReplays;
        }
        
        public Parameters setIncludeReplays(Boolean includeReplays) {
            this.includeReplays = includeReplays;
            return this;
        }
        
        public Boolean getIncludeIndeterminate() {
            return includeIndeterminate;
        }
        
        public Parameters setIncludeIndeterminate(Boolean includeIndeterminate) {
            this.includeIndeterminate = includeIndeterminate;
            return this;
        }
    }
    
}
