package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseCount;

/**
 * This request class is used to request the number of delegators for a specified representative.
 * <br>Calls the RPC command {@code delegators_count}, and returns a {@link ResponseCount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#delegators_count">Official RPC documentation</a>
 */
public class RequestDelegatorsCount extends RpcRequest<ResponseCount> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the representative's address
     */
    public RequestDelegatorsCount(String account) {
        super("delegators_count", ResponseCount.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested representative's address
     */
    public String getAccount() {
        return account;
    }
    
}
