package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to convert a public key into a standardized account address.
 * The server responds with a {@link ResponseAccount} data object.<br>
 * Calls the internal RPC method {@code account_get}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_get">Official RPC documentation</a>
 */
public class RequestAccountFromKey extends RpcRequest<ResponseAccount> {
    
    @Expose @SerializedName("key")
    private final String accountKey;
    
    
    /**
     * @param accountKey the account's public key
     */
    public RequestAccountFromKey(String accountKey) {
        super("account_get", ResponseAccount.class);
        this.accountKey = accountKey;
    }
    
    
    /**
     * @return the requested account's public key
     */
    public String getAccountKey() {
        return accountKey;
    }
}
