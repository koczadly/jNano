package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletExport;

/**
 * This request class is used to export a wallet as a JSON string.
 * The server responds with a {@link ResponseWalletExport} data object.<br>
 * Calls the internal RPC method {@code wallet_export}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_export">Official RPC documentation</a>
 */
public class RequestWalletExport extends RpcRequest<ResponseWalletExport> {
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletExport(String walletId) {
        super("wallet_export", ResponseWalletExport.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
