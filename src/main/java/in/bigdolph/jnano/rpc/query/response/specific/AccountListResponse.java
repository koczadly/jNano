package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AccountListResponse extends RPCResponse {

    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
}
