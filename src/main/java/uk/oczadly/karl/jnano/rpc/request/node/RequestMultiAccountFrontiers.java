package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseMultiAccountFrontiers;

/**
 * This request class is used to fetch the head block for the specified accounts.
 * The server responds with a {@link ResponseMultiAccountFrontiers} data object.<br>
 * Calls the internal RPC method {@code accounts_frontiers}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#accounts_frontiers">Official RPC documentation</a>
 */
public class RequestMultiAccountFrontiers extends RpcRequest<ResponseMultiAccountFrontiers> {
    
    @Expose @SerializedName("accounts")
    private final String[] accounts;
    
    
    /**
     * @param accounts the accounts' addresses
     */
    public RequestMultiAccountFrontiers(String... accounts) {
        super("accounts_frontiers", ResponseMultiAccountFrontiers.class);
        this.accounts = accounts;
    }
    
    
    /**
     * @return the requested accounts' addresses
     */
    public String[] getAccounts() {
        return accounts;
    }
    
}
