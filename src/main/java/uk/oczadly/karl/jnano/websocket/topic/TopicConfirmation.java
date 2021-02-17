/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.websocket.topic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.websocket.NanoWebSocketClient;
import uk.oczadly.karl.jnano.websocket.TopicWithUpdateParams;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageConfirmation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The {@code confirmation} WebSocket topic.
 * <p>This topic supports both subscription and update parameters with the use of the {@link SubArgs} and
 * {@link UpdateArgs} subclasses.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageConfirmation} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#confirmation">
 *     Official WebSocket documentation</a>
 */
public class TopicConfirmation extends TopicWithUpdateParams<TopicMessageConfirmation, TopicConfirmation.SubArgs,
        TopicConfirmation.UpdateArgs> {
    
    public TopicConfirmation(NanoWebSocketClient client) {
        super("confirmation", TopicMessageConfirmation.class, client);
    }
    
    
    public enum ConfirmationType {
        ALL,
        ACTIVE,
        INACTIVE,
        ACTIVE_QUORUM,
        ACTIVE_CONFIRMATION_HEIGHT
    }
    
    /**
     * The configuration parameters when subscribing to this topic.
     */
    public static final class SubArgs {
        @Expose @SerializedName("confirmation_type")
        private ConfirmationType type;
        
        @Expose @SerializedName("all_local_accounts")
        private Boolean allLocalAccounts;
        
        @Expose @SerializedName("accounts")
        private Collection<NanoAccount> accounts;
        
        @Expose @SerializedName("include_block")
        private Boolean includeBlock;
        
        @Expose @SerializedName("include_election_info")
        private Boolean includeElectionInfo;
    
    
        /**
         * Sets the filter for the confirmation type.
         * @param type the confirmation type, or null for default behaviour
         * @return this argument builder
         */
        public SubArgs filterConfirmationType(ConfirmationType type) {
            this.type = type;
            return this;
        }
    
        /**
         * Sets the accounts filter. Only confirmed blocks held by these accounts will trigger a notification.
         * @param accounts a list of account addresses, or null to disable the filter
         * @return this argument builder
         */
        public SubArgs filterAccounts(Collection<NanoAccount> accounts) {
            this.accounts = accounts;
            return this;
        }
    
        /**
         * Sets the accounts filter. Only confirmed blocks held by these accounts will trigger a notification.
         * @param accounts an array of account addresses
         * @return this argument builder
         */
        public SubArgs filterAccounts(NanoAccount... accounts) {
            return filterAccounts(Arrays.asList(accounts));
        }
    
        /**
         * Sets the accounts filter. Only confirmed blocks held by these accounts will trigger a notification.
         * @param accounts a list of account addresses (parsed using {@link NanoAccount#parse(String)})
         * @return this argument builder
         */
        public SubArgs filterAccounts(String... accounts) {
            return filterAccounts(
                    Arrays.stream(accounts)
                            .map(NanoAccount::parse)
                            .collect(Collectors.toList()));
        }
    
        /**
         * Enables the accounts filter, and includes all accounts from the Node's local wallets.
         * @return this argument builder
         */
        public SubArgs includeLocalAccounts() {
            this.allLocalAccounts = true;
            return this;
        }
    
        /**
         * Enables the block contents being included in the messages.
         * @return this argument builder
         */
        public SubArgs includeBlockContents() {
            this.includeBlock = true;
            return this;
        }
    
        /**
         * Enables the election information being included in the messages.
         * @return this argument builder
         */
        public SubArgs includeElectionInfo() {
            this.includeElectionInfo = true;
            return this;
        }
    }
    
    /**
     * The configuration parameters when updating this topic's configuration.
     */
    public static final class UpdateArgs {
        @Expose @SerializedName("accounts_add")
        private final Collection<NanoAccount> accountsAdd = new ArrayList<>();
        
        @Expose @SerializedName("accounts_del")
        private final Collection<NanoAccount> accountsRemove = new ArrayList<>();
    
    
        /**
         * Adds a list of accounts to the accounts filter.
         * @param accounts the accounts to add to the filter
         * @return this argument builder
         */
        public UpdateArgs addAccountsFilter(Collection<NanoAccount> accounts) {
            this.accountsAdd.addAll(accounts);
            return this;
        }
    
        /**
         * Adds a list of accounts to the accounts filter.
         * @param accounts the accounts to add to the filter
         * @return this argument builder
         */
        public UpdateArgs addAccountsFilter(NanoAccount... accounts) {
            return addAccountsFilter(Arrays.asList(accounts));
        }
    
        /**
         * Adds a list of accounts to the accounts filter.
         * @param accounts the accounts to add to the filter (parsed using {@link NanoAccount#parse(String)})
         * @return this argument builder
         */
        public UpdateArgs addAccountsFilter(String... accounts) {
            return addAccountsFilter(
                    Arrays.stream(accounts)
                            .map(NanoAccount::parse)
                            .collect(Collectors.toList()));
        }
    
        /**
         * Removes a list of accounts from the accounts filter.
         * @param accounts the accounts to remove from the filter
         * @return this argument builder
         */
        public UpdateArgs removeAccountsFilter(Collection<NanoAccount> accounts) {
            this.accountsRemove.addAll(accounts);
            return this;
        }
    
        /**
         * Removes a list of accounts from the accounts filter.
         * @param accounts the accounts to remove from the filter
         * @return this argument builder
         */
        public UpdateArgs removeAccountsFilter(NanoAccount... accounts) {
            return removeAccountsFilter(Arrays.asList(accounts));
        }
    
        /**
         * Removes a list of accounts from the accounts filter.
         * @param accounts the accounts to remove from the filter (parsed using {@link NanoAccount#parse(String)})
         * @return this argument builder
         */
        public UpdateArgs removeAccountsFilter(String... accounts) {
            return removeAccountsFilter(
                    Arrays.stream(accounts)
                            .map(NanoAccount::parse)
                            .collect(Collectors.toList()));
        }
    }
    
}
