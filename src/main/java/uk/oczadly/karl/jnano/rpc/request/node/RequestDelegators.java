package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseDelegators;

/**
 * This request class is used to fetch a list of delegators for the specified representative account.
 * The server responds with a {@link ResponseDelegators} data object.<br>
 * Calls the internal RPC method {@code delegators}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#delegators">Official RPC documentation</a>
 */
public class RequestDelegators extends RpcRequest<ResponseDelegators> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the representative's address
     */
    public RequestDelegators(String account) {
        super("delegators", ResponseDelegators.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested representative's address
     */
    public String getAccount() {
        return account;
    }
    
}
