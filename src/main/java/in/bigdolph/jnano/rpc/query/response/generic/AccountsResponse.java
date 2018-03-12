package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Set;

public class AccountsResponse extends RPCResponse {

    @Expose
    @SerializedName("accounts")
    private Set<String> accounts;
    
    
    
    public Set<String> getAccounts() {
        return accounts;
    }
    
}
