package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to manually search for pending blocks in the specified local wallet.
 * <br>Calls the RPC command {@code search_pending}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#search_pending">Official RPC documentation</a>
 */
public class RequestSearchPending extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestSearchPending(String walletId) {
        super("search_pending", ResponseSuccessful.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
