package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This response class contains a single wallet ID string.
 */
public class ResponseWalletCreate extends RpcResponse {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("last_restored_account")
    private NanoAccount lastAccount;
    
    @Expose @SerializedName("restored_count")
    private Integer restoredCount;
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the newest account imported to this wallet
     */
    public NanoAccount getLastAccount() {
        return lastAccount;
    }
    
    /**
     * @return the number of accounts imported into this wallet
     */
    public int getRestoredCount() {
        return restoredCount != null ? restoredCount : 0;
    }
    
}
