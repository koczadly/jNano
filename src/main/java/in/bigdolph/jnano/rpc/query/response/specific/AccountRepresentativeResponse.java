package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class AccountRepresentativeResponse extends RPCResponse {

    @Expose
    @SerializedName("representative")
    private String representativeAddress;
    
    
    
    public String getRepresentativeAddress() {
        return representativeAddress;
    }
    
}
