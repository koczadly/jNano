package uk.oczadly.karl.jnano.websocket.topic.options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.websocket.topic.message.TopicMessageConfirmation;

import java.util.List;

public class TopicOptionsConfirmation {

    @Expose @SerializedName("confirmation_type")
    private TopicMessageConfirmation.ConfirmationType type;
    
    @Expose @SerializedName("all_local_accounts")
    private Boolean allLocalAccounts;
    
    @Expose @SerializedName("accounts")
    private List<String> accounts;
    
    @Expose @SerializedName("include_block")
    private Boolean includeBlock;
    
    @Expose @SerializedName("include_election_info")
    private Boolean includeElectionInfo;
    
    
    public TopicMessageConfirmation.ConfirmationType getConfirmationType() {
        return type;
    }
    
    public TopicOptionsConfirmation setConfirmationType(TopicMessageConfirmation.ConfirmationType type) {
        this.type = type;
        return this;
    }
    
    public Boolean getAllLocalAccounts() {
        return allLocalAccounts;
    }
    
    public TopicOptionsConfirmation setAllLocalAccounts(Boolean allLocalAccounts) {
        this.allLocalAccounts = allLocalAccounts;
        return this;
    }
    
    public List<String> getAccounts() {
        return accounts;
    }
    
    public TopicOptionsConfirmation setAccounts(List<String> accounts) {
        this.accounts = accounts;
        return this;
    }
    
    public Boolean getIncludeBlock() {
        return includeBlock;
    }
    
    public TopicOptionsConfirmation setIncludeBlock(Boolean includeBlock) {
        this.includeBlock = includeBlock;
        return this;
    }
    
    public Boolean getIncludeElectionInfo() {
        return includeElectionInfo;
    }
    
    public TopicOptionsConfirmation setIncludeElectionInfo(Boolean includeElectionInfo) {
        this.includeElectionInfo = includeElectionInfo;
        return this;
    }
}
