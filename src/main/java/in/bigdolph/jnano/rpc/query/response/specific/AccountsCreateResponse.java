package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.util.List;
import java.util.Set;

public class AccountsCreateResponse extends RPCResponse {

    @Expose
    @SerializedName("accounts")
    private Set<String> accounts;
    
    
    
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
