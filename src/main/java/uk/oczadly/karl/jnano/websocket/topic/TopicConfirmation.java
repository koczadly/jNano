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
import uk.oczadly.karl.jnano.websocket.TopicWithUpdateParams;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageConfirmation;

import java.util.Arrays;
import java.util.List;

/**
 * The {@code confirmation} WebSocket topic.
 * <p>This topic supports both subscription and update parameters with the use of the {@link SubParams} and
 * {@link UpdateParams} subclasses.</p>
 * <p>Received data messages will be encoded in the {@link TopicMessageConfirmation} class.</p>
 *
 * @see <a href="https://docs.nano.org/integration-guides/websockets/#confirmation">
 *     Official WebSocket documentation</a>
 */
public class TopicConfirmation extends Topic<TopicMessageConfirmation>
        implements TopicWithUpdateParams<TopicConfirmation.SubParams, TopicConfirmation.UpdateParams> {
    
    public TopicConfirmation(NanoWebSocketClient client) {
        super("confirmation", client, TopicMessageConfirmation.class);
    }
    
    @Override
    public void subscribe(SubParams params) {
        _subscribe(params);
    }
    
    @Override
    public boolean subscribeBlocking(long timeout, SubParams params) throws InterruptedException {
        return _subscribeBlocking(timeout, params);
    }
    
    @Override
    public void update(UpdateParams params) {
        _update(params);
    }
    
    @Override
    public boolean updateBlocking(long timeout, UpdateParams params) throws InterruptedException {
        return _updateBlocking(timeout, params);
    }
    
    
    /**
     * The configuration parameters when subscribing to this topic.
     */
    public static final class SubParams {
        @Expose @SerializedName("confirmation_type")
        private TopicMessageConfirmation.ConfirmationType type;
        
        @Expose @SerializedName("all_local_accounts")
        private Boolean allLocalAccounts;
        
        @Expose @SerializedName("accounts")
        private List<NanoAccount> accounts;
        
        @Expose @SerializedName("include_block")
        private Boolean includeBlock;
        
        @Expose @SerializedName("include_election_info")
        private Boolean includeElectionInfo;
        
        
        public TopicMessageConfirmation.ConfirmationType getConfirmationType() {
            return type;
        }
        
        public SubParams setConfirmationType(TopicMessageConfirmation.ConfirmationType type) {
            this.type = type;
            return this;
        }
        
        public Boolean getAllLocalAccounts() {
            return allLocalAccounts;
        }
        
        public SubParams setAllLocalAccounts(Boolean allLocalAccounts) {
            this.allLocalAccounts = allLocalAccounts;
            return this;
        }
        
        public List<NanoAccount> getAccounts() {
            return accounts;
        }
        
        public SubParams setAccounts(List<NanoAccount> accounts) {
            this.accounts = accounts;
            return this;
        }
        
        public SubParams setAccounts(NanoAccount... accounts) {
            return setAccounts(Arrays.asList(accounts));
        }
        
        public Boolean getIncludeBlock() {
            return includeBlock;
        }
        
        public SubParams setIncludeBlock(Boolean includeBlock) {
            this.includeBlock = includeBlock;
            return this;
        }
        
        public Boolean getIncludeElectionInfo() {
            return includeElectionInfo;
        }
        
        public SubParams setIncludeElectionInfo(Boolean includeElectionInfo) {
            this.includeElectionInfo = includeElectionInfo;
            return this;
        }
    }
    
    /**
     * The configuration parameters when updating this topic's configuration.
     */
    public static final class UpdateParams {
        @Expose @SerializedName("accounts_add")
        private List<NanoAccount> accountsAdd;
        
        @Expose @SerializedName("accounts_del")
        private List<NanoAccount> accountsRemove;
        
        
        public List<NanoAccount> getAccountsAdd() {
            return accountsAdd;
        }
        
        public UpdateParams setAccountsAdd(List<NanoAccount> accountsAdd) {
            this.accountsAdd = accountsAdd;
            return this;
        }
        
        public UpdateParams setAccountsAdd(NanoAccount... accountsAdd) {
            return setAccountsAdd(Arrays.asList(accountsAdd));
        }
        
        public List<NanoAccount> getAccountsRemove() {
            return accountsRemove;
        }
        
        public UpdateParams setAccountsRemove(List<NanoAccount> accountsRemove) {
            this.accountsRemove = accountsRemove;
            return this;
        }
        
        public UpdateParams setAccountsRemove(NanoAccount... accountsAdd) {
            return setAccountsRemove(Arrays.asList(accountsAdd));
        }
    }
    
}
