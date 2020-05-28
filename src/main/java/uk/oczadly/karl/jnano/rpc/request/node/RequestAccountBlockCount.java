package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccountBlockCount;

/**
 * This request class is used to request the number of blocks for a specific account.
 * <br>Calls the RPC command {@code account_block_count}, and returns a {@link ResponseAccountBlockCount} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#account_block_count">Official RPC documentation</a>
 */
public class RequestAccountBlockCount extends RpcRequest<ResponseAccountBlockCount> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    
    /**
     * @param account the account's address
     */
    public RequestAccountBlockCount(String account) {
        super("account_block_count", ResponseAccountBlockCount.class);
        this.account = account;
    }
    
    
    /**
     * @return the requested account's address
     */
    public String getAccount() {
        return account;
    }
}
