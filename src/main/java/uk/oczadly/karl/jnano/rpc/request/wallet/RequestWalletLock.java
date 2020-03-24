package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletLocked;

/**
 * This request class locks and re-encrypts the specified wallet.
 * <br>Calls the RPC command {@code wallet_lock}, and returns a {@link ResponseWalletLocked} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_lock">Official RPC documentation</a>
 */
public class RequestWalletLock extends RpcRequest<ResponseWalletLocked> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletLock(String walletId) {
        super("wallet_lock", ResponseWalletLocked.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
