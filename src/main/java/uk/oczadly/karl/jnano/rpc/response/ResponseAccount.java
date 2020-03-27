package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains a single account address.
 */
public class ResponseAccount extends RpcResponse {
    
    @Expose @SerializedName(value = "account", alternate = {"representative"})
    private String address;
    
    
    /**
     * @return the account's address
     */
    public String getAccountAddress() {
        return address;
    }
    
}
