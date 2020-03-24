package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to remove an account from a local wallet.
 * <br>Calls the RPC command {@code account_remove}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_remove">Official RPC documentation</a>
 */
public class RequestAccountRemove extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param walletId  the containing wallet's ID
     * @param account   the account's address
     */
    public RequestAccountRemove(String walletId, String account) {
        super("account_remove", ResponseSuccessful.class);
        this.walletId = walletId;
        this.account = account;
    }
    
    
    /**
     * @return the requested wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the requested account
     */
    public String getAccount() {
        return account;
    }
    
}
