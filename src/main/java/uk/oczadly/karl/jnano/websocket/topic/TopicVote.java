/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.TopicWithSubParams;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageVote;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code vote} WebSocket topic.
 * <p>This topic supports subscription parameters with the use of the {@link SubArgs} subclass, but does not
 * support updating of these parameters.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageVote} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#votes">
 *     Official WebSocket documentation</a>
 */
public class TopicVote extends TopicWithSubParams<TopicMessageVote, TopicVote.SubArgs> {
    
    public TopicVote(NanoWebSocketClient client) {
        super("vote", client, TopicMessageVote.class);
    }
    
    
    /**
     * The configuration parameters when subscribing to this topic.
     */
    public static final class SubArgs {
        @Expose @SerializedName("representatives")
        private List<NanoAccount> representatives;
        
        @Expose @SerializedName("include_replays")
        private Boolean includeReplays;
        
        @Expose @SerializedName("include_indeterminate")
        private Boolean includeIndeterminate;
    
    
        /**
         * Sets the representatives filter. Only votes by the specified representative accounts will trigger a
         * notification.
         * @param representatives a list of representative addresses, or null to disable the filter
         * @return this argument builder
         */
        public SubArgs filterRepresentatives(List<NanoAccount> representatives) {
            this.representatives = representatives;
            return this;
        }
    
        /**
         * Sets the representatives filter. Only votes by the specified representative accounts will trigger a
         * notification.
         * @param representatives an array of representative addresses
         * @return this argument builder
         */
        public SubArgs filterRepresentatives(NanoAccount... representatives) {
            return filterRepresentatives(Arrays.asList(representatives));
        }
    
        /**
         * Sets the representatives filter. Only votes by the specified representative accounts will trigger a
         * notification.
         * @param representatives an array of representative addresses (parsed using {@link NanoAccount#parse(String)})
         * @return this argument builder
         */
        public SubArgs filterRepresentatives(String... representatives) {
            return filterRepresentatives(
                    Arrays.stream(representatives)
                            .map(NanoAccount::parse)
                            .collect(Collectors.toList()));
        }
    
        /**
         * Enables replay votes to be included.
         * @return this argument builder
         */
        public SubArgs includeReplays() {
            this.includeReplays = true;
            return this;
        }
    
        /**
         * Enables indeterminate votes to be included.
         * @return this argument builder
         */
        public SubArgs includeIndeterminate() {
            this.includeIndeterminate = true;
            return this;
        }
    }
    
}
