package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a string produced when exporting a wallet.
 */
public class ResponseWalletExport extends RpcResponse {

    @Expose @SerializedName("json")
    private String exportedJson;
    
    
    /**
     * @return a string of the exported wallet in JSON format
     */
    public String getExportedJson() {
        return exportedJson;
    }
    
}
