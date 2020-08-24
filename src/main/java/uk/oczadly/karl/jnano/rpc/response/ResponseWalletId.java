package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.gsonadapters.SingleValueJsonAdapter;

/**
 * This response class contains a single wallet ID string.
 */
@JsonAdapter(SingleValueJsonAdapter.class)
public class ResponseWalletId extends RpcResponse {
    
    @Expose
    private String walletId;
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
