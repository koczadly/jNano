package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

/**
 * This request class is used to request a list of accounts in the ledger, and their head block.
 * <br>Calls the RPC command {@code frontiers}, and returns a {@link ResponseMultiAccountFrontiers} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#frontiers">Official RPC documentation</a>
 */
public class RequestFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    /**
     * @param account the account's address to start at
     * @param count   the number of frontiers to list
     */
    public RequestFrontiers(String account, int count) {
        super("frontiers", ResponseMultiAccountFrontiers.class);
        this.account = account;
        this.count = count;
    }
    
    
    /**
     * @return the requested starting account
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the requested limit
     */
    public int getCount() {
        return count;
    }
    
}
