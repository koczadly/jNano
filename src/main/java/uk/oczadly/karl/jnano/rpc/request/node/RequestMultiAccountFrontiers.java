package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

public class RequestMultiAccountFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    public RequestMultiAccountFrontiers(String... accounts) {
        super("accounts_frontiers", ResponseMultiAccountFrontiers.class);
        this.accounts = accounts;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
