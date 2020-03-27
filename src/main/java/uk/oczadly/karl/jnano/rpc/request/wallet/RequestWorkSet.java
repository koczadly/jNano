package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to set the pre-computed work for a specified local account.
 * <br>Calls the RPC command {@code work_set}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_set">Official RPC documentation</a>
 */
public class RequestWorkSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("work")
    private final String workSolution;
    
    
    /**
     * @param walletId     the wallet's ID
     * @param account      the account's address
     * @param workSolution a pre-computed work solution
     */
    public RequestWorkSet(String walletId, String account, String workSolution) {
        super("work_set", ResponseSuccessful.class);
        this.walletId = walletId;
        this.account = account;
        this.workSolution = workSolution;
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
    
    /**
     * @return a pre-computed work solution
     */
    public String getWorkSolution() {
        return workSolution;
    }
    
}
