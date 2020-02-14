package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccounts;

/**
 * This request class is used to fetch a list of accounts inside in the given wallet.
 * The server responds with a {@link ResponseAccounts} data object.<br>
 * Calls the internal RPC method {@code account_list}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_list">Official RPC documentation</a>
 */
public class RequestAccountList extends RpcRequest<ResponseAccounts> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestAccountList(String walletId) {
        super("account_list", ResponseAccounts.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
