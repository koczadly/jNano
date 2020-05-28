package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains a single wallet ID string.
 */
public class ResponseWalletId extends RpcResponse {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
