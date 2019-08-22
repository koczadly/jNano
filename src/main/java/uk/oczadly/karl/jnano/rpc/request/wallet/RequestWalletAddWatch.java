package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to add watch-only accounts to the a local wallet.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code wallet_add_watch}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_add_watch">Official RPC documentation</a>
 */
public class RequestWalletAddWatch extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("accounts")
    private final String[] accounts;
    
    
    /**
     * @param walletId  the wallet's ID
     * @param accounts  the accounts' addresses
     */
    public RequestWalletAddWatch(String walletId, String... accounts) {
        super("wallet_add_watch", ResponseSuccessful.class);
        this.walletId = walletId;
        this.accounts = accounts;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the accounts' addresses
     */
    public String[] getAccounts() {
        return accounts;
    }
    
}
