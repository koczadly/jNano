package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class AccountsResponse extends RpcResponse {

    @Expose
    @SerializedName("accounts")
    private Set<String> accounts;
    
    
    
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
