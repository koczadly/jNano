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
import uk.oczadly.karl.jnano.websocket.WsTopic;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageConfirmation;

import java.util.Arrays;
import java.util.List;

public class WsTopicConfirmation extends WsTopic<TopicMessageConfirmation>
        implements TopicWithUpdateParams<WsTopicConfirmation.SubParameters, WsTopicConfirmation.UpdateOptions> {
    
    public WsTopicConfirmation(NanoWebSocketClient client) {
        super("confirmation", client, TopicMessageConfirmation.class);
    }
    
    @Override
    public void subscribe(SubParameters params) {
        _subscribe(params);
    }
    
    @Override
    public boolean subscribeBlocking(long timeout, SubParameters params) throws InterruptedException {
        return _subscribeBlocking(timeout, params);
    }
    
    @Override
    public void update(UpdateOptions params) {
        _update(params);
    }
    
    @Override
    public boolean updateBlocking(long timeout, UpdateOptions params) throws InterruptedException {
        return _updateBlocking(timeout, params);
    }
    
    
    
    public static final class SubParameters {
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
        
        public SubParameters setConfirmationType(TopicMessageConfirmation.ConfirmationType type) {
            this.type = type;
            return this;
        }
        
        public Boolean getAllLocalAccounts() {
            return allLocalAccounts;
        }
        
        public SubParameters setAllLocalAccounts(Boolean allLocalAccounts) {
            this.allLocalAccounts = allLocalAccounts;
            return this;
        }
        
        public List<NanoAccount> getAccounts() {
            return accounts;
        }
        
        public SubParameters setAccounts(List<NanoAccount> accounts) {
            this.accounts = accounts;
            return this;
        }
        
        public SubParameters setAccounts(NanoAccount... accounts) {
            return setAccounts(Arrays.asList(accounts));
        }
        
        public Boolean getIncludeBlock() {
            return includeBlock;
        }
        
        public SubParameters setIncludeBlock(Boolean includeBlock) {
            this.includeBlock = includeBlock;
            return this;
        }
        
        public Boolean getIncludeElectionInfo() {
            return includeElectionInfo;
        }
        
        public SubParameters setIncludeElectionInfo(Boolean includeElectionInfo) {
            this.includeElectionInfo = includeElectionInfo;
            return this;
        }
    }
    
    public static final class UpdateOptions {
        @Expose @SerializedName("accounts_add")
        private List<NanoAccount> accountsAdd;
        
        @Expose @SerializedName("accounts_del")
        private List<NanoAccount> accountsRemove;
        
        
        public List<NanoAccount> getAccountsAdd() {
            return accountsAdd;
        }
        
        public UpdateOptions setAccountsAdd(List<NanoAccount> accountsAdd) {
            this.accountsAdd = accountsAdd;
            return this;
        }
        
        public UpdateOptions setAccountsAdd(NanoAccount... accountsAdd) {
            return setAccountsAdd(Arrays.asList(accountsAdd));
        }
        
        public List<NanoAccount> getAccountsRemove() {
            return accountsRemove;
        }
        
        public UpdateOptions setAccountsRemove(List<NanoAccount> accountsRemove) {
            this.accountsRemove = accountsRemove;
            return this;
        }
        
        public UpdateOptions setAccountsRemove(NanoAccount... accountsAdd) {
            return setAccountsRemove(Arrays.asList(accountsAdd));
        }
    }
    
}
