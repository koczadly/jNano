package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUncheckedKeys;

/**
 * This request class is used to retrieve unchecked database keys and blocks.
 * <br>Calls the RPC command {@code unchecked_keys}, and returns a {@link ResponseUncheckedKeys} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#unchecked_keys">Official RPC documentation</a>
 */
public class RequestUncheckedKeys extends RpcRequest<ResponseUncheckedKeys> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    
    @Expose @SerializedName("key")
    private final String key;
    
    @Expose @SerializedName("count")
    private final int count;
    
    
    /**
     * @param key   the key to look up
     * @param count the result limit
     */
    public RequestUncheckedKeys(String key, int count) {
        super("unchecked_keys", ResponseUncheckedKeys.class);
        this.key = key;
        this.count = count;
    }
    
    
    /**
     * @return the requested key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * @return the requested limit
     */
    public int getCount() {
        return count;
    }
    
}
