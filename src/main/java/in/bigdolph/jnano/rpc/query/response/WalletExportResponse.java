package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletExportResponse extends RpcResponse {

    @Expose
    @SerializedName("json")
    private String exportedJson;
    
    
    
    public String getExportedJson() {
        return exportedJson;
    }
    
}
