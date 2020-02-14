package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountInfo;

/**
 * This request class is used to request an account's information.
 * The server responds with a {@link ResponseAccountInfo} data object.<br>
 * Calls the internal RPC method {@code account_info}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_info">Official RPC documentation</a>
 */
public class RequestAccountInfo extends RpcRequest<ResponseAccountInfo> {
    
    @Expose @SerializedName("representative")
    private final boolean fetchRepresentative = true;
    
    @Expose @SerializedName("weight")
    private final boolean fetchWeight = true;
    
    @Expose @SerializedName("pending")
    private final boolean fetchPending = true;
    
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the account's address
     */
    public RequestAccountInfo(String account) {
        super("account_info", ResponseAccountInfo.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
    
}
