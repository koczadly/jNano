package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletFetchDefaultRepresentativeResponse extends RPCResponse {

    @Expose
    @SerializedName("representative")
    private String representativeAccount;
    
    
    
    public String getRepresentativeAccount() {
        return representativeAccount;
    }
    
}
