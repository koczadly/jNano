package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletLocked;

/**
 * This request class is used to check the locked state of a wallet.
 * The server responds with a {@link ResponseWalletLocked} data object.<br>
 * Calls the internal RPC method {@code wallet_locked}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_locked">Official RPC documentation</a>
 */
public class RequestWalletLocked extends RpcRequest<ResponseWalletLocked> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletLocked(String walletId) {
        super("wallet_locked", ResponseWalletLocked.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
