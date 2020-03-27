package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseWork;

/**
 * This request class is used to retrieve the pre-computed work solution for a specified local account.
 * <br>Calls the RPC command {@code work_get}, and returns a {@link ResponseWork} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_get">Official RPC documentation</a>
 */
public class RequestWorkGet extends RpcRequest<ResponseWork> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param walletId the wallet's ID
     * @param account  the account's address
     */
    public RequestWorkGet(String walletId, String account) {
        super("work_get", ResponseWork.class);
        this.walletId = walletId;
        this.account = account;
    }
    
    
    /**
     * @return the wallet's ID
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the account's address
     */
    public String getAccount() {
        return account;
    }
    
}
