package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class AccountRemoveResponse extends RPCResponse {

    @Expose
    @SerializedName("removed")
    private int accountsRemoved;
    
    
    
    public int getAccountsRemoved() {
        return accountsRemoved;
    }
    
}
