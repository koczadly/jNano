package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletWork;

/**
 * This request class is used to fetch the pre-computed work for every existing account within the specified wallet.
 * The server responds with a {@link ResponseWalletWork} data object.<br>
 * Calls the internal RPC method {@code wallet_work_get}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_work_get">Official RPC documentation</a>
 */
public class RequestWalletWorkGet extends RpcRequest<ResponseWalletWork> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletWorkGet(String walletId) {
        super("wallet_work_get", ResponseWalletWork.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
