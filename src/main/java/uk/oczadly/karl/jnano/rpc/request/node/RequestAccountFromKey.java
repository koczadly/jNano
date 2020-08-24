package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

/**
 * This request class is used to convert a public key into a standardized account address.
 * <br>Calls the RPC command {@code account_get}, and returns a {@link ResponseAccount} data object.
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
