package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWalletInfo;

/**
 * This request class is used to retrieve summarized information about all of the accounts within a given wallet.
 * <br>Calls the RPC command {@code wallet_info}, and returns a {@link ResponseWalletInfo} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#wallet_info">Official RPC documentation</a>
 */
public class RequestWalletInfo extends RpcRequest<ResponseWalletInfo> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    
    /**
     * @param walletId the wallet's ID
     */
    public RequestWalletInfo(String walletId) {
        super("wallet_info", ResponseWalletInfo.class);
        this.walletId = walletId;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
}
