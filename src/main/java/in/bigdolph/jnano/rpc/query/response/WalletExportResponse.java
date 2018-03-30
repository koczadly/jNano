package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class WalletExportResponse extends RPCResponse {

    @Expose
    @SerializedName("json")
    private String exportedJson;
    
    
    
    public String getExportedJson() {
        return exportedJson;
    }
    
}
