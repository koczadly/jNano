package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUnopened;

/**
 * This request class is used to fetch a list of unopened accounts with a pending balance.
 * <br>Calls the RPC command {@code unopened}, and returns a {@link ResponseUnopened} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unopened">Official RPC documentation</a>
 */
public class RequestUnopened extends RpcRequest<ResponseUnopened> {
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("count")
    private final Integer count;
    
    
    public RequestUnopened() {
        this(null, null);
    }
    
    /**
     * @param account (optional) the starting account's address
     * @param count   (optional) the result limit
     */
    public RequestUnopened(String account, Integer count) {
        super("unopened", ResponseUnopened.class);
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
     * @return the requested result limit
     */
    public int getCount() {
        return count;
    }
    
}
