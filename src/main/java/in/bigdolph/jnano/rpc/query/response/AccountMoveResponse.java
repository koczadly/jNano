package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;

public class AccountMoveResponse extends RPCResponse {

    @Expose
    @SerializedName("moved")
    private int accountsMoved;
    
    
    
    public int getAccountsMoved() {
        return accountsMoved;
    }
    
}
