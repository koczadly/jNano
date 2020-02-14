package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBalance;

/**
 * This request class is used to request an account's balance.
 * The server responds with a {@link ResponseBalance} data object.<br>
 * Calls the internal RPC method {@code account_balance}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_balance">Official RPC documentation</a>
 */
public class RequestAccountBalance extends RpcRequest<ResponseBalance> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account   the account's address
     */
    public RequestAccountBalance(String account) {
        super("account_balance", ResponseBalance.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's request
     */
    public String getAccount() {
        return account;
    }
    
}
