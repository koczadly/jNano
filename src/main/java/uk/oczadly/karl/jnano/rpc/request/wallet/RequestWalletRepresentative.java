package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to fetch the current configured representative for the specified wallet.
 * The server responds with a {@link ResponseAccount} data object.<br>
 * Calls the internal RPC method {@code wallet_representative}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_representative">Official RPC documentation</a>
 */
public class RequestWalletRepresentative extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletRepresentative(String walletId) {
        super("wallet_representative", ResponseAccount.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
