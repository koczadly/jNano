package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AccountResponse extends RPCResponse {

    @Expose
    @SerializedName("account")
    private String accountAddress;
    
    
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
}
