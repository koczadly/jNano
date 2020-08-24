package uk.oczadly.karl.jnano.websocket.topic.options;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.List;

public class TopicUpdateConfirmation {

    @Expose @SerializedName("accounts_add")
    private List<NanoAccount> accountsAdd;
    
    @Expose @SerializedName("accounts_del")
    private List<NanoAccount> accountsRemove;
    
    
    public List<NanoAccount> getAccountsAdd() {
        return accountsAdd;
    }
    
    public TopicUpdateConfirmation setAccountsAdd(List<NanoAccount> accountsAdd) {
        this.accountsAdd = accountsAdd;
        return this;
    }
    
    public List<NanoAccount> getAccountsRemove() {
        return accountsRemove;
    }
    
    public TopicUpdateConfirmation setAccountsRemove(List<NanoAccount> accountsRemove) {
        this.accountsRemove = accountsRemove;
        return this;
    }
    
}
