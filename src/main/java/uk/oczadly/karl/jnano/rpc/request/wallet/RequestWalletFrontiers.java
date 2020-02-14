package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

/**
 * This request class is used to fetch a list of accounts within a wallet, and their head block hashes.
 * The server responds with a {@link ResponseMultiAccountFrontiers} data object.<br>
 * Calls the internal RPC method {@code wallet_frontiers}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_frontiers">Official RPC documentation</a>
 */
public class RequestWalletFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletFrontiers(String walletId) {
        super("wallet_frontiers", ResponseMultiAccountFrontiers.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
