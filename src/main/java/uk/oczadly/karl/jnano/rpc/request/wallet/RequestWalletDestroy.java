package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to destroy an existing local wallet.
 * <br>Calls the RPC command {@code wallet_destroy}, and returns a {@link ResponseSuccessful} data object.
 * <br><br>
 * Caution: this will destroy all seeds and private keys associated with the wallet!
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_destroy">Official RPC documentation</a>
 */
public class RequestWalletDestroy extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletDestroy(String walletId) {
        super("wallet_destroy", ResponseSuccessful.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
