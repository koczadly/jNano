package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to request an account's current representative.
 * <br>Calls the RPC command {@code account_representative}, and returns a {@link ResponseAccount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_representative">Official RPC documentation</a>
 */
public class RequestAccountRepresentative extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the account's address
     */
    public RequestAccountRepresentative(String account) {
        super("account_representative", ResponseAccount.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
    
}
