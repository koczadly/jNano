/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.Topic;
import uk.oczadly.karl.jnano.websocket.TopicWithSubscribeParams;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageVote;

import java.util.Arrays;
import java.util.List;

/**
 * The {@code vote} WebSocket topic.
 * <p>This topic supports subscription parameters with the use of the {@link SubParams} subclass, but does not
 * support updating of these parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageVote} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#votes">
 *     Official WebSocket documentation</a>
 */
public class TopicVote extends Topic<TopicMessageVote>
        implements TopicWithSubscribeParams<TopicVote.SubParams> {
    
    public TopicVote(NanoWebSocketClient client) {
        super("vote", client, TopicMessageVote.class);
    }
    
    
    @Override
    public void subscribe(SubParams params) {
        _subscribe(params);
    }
    
    @Override
    public boolean subscribeBlocking(long timeout, SubParams params) throws InterruptedException {
        return _subscribeBlocking(timeout, params);
    }
    
    
    
    /**
     * The configuration parameters when subscribing to this topic.
     */
    public static final class SubParams {
        @Expose @SerializedName("representatives")
        private List<NanoAccount> representatives;
        
        @Expose @SerializedName("include_replays")
        private Boolean includeReplays;
        
        @Expose @SerializedName("include_indeterminate")
        private Boolean includeIndeterminate;
        
        
        public List<NanoAccount> getRepresentatives() {
            return representatives;
        }
        
        public SubParams setRepresentatives(List<NanoAccount> representatives) {
            this.representatives = representatives;
            return this;
        }
        
        public SubParams setRepresentatives(NanoAccount... representatives) {
            return setRepresentatives(Arrays.asList(representatives));
        }
        
        public Boolean getIncludeReplays() {
            return includeReplays;
        }
        
        public SubParams setIncludeReplays(Boolean includeReplays) {
            this.includeReplays = includeReplays;
            return this;
        }
        
        public Boolean getIncludeIndeterminate() {
            return includeIndeterminate;
        }
        
        public SubParams setIncludeIndeterminate(Boolean includeIndeterminate) {
            this.includeIndeterminate = includeIndeterminate;
            return this;
        }
    }
    
}
