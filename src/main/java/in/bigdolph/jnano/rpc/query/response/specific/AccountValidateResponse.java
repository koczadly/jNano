package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class AccountValidateResponse extends RPCResponse {

    @Expose
    @SerializedName("valid")
    private boolean isValid;
    
    
    public boolean isAccountValid() {
        return isValid;
    }
    
}
