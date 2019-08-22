package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseRepresentatives;

/**
 * This request class is used to fetch a list of representatives and their voting weight.
 * The server responds with a {@link ResponseRepresentatives} data object.<br>
 * Calls the internal RPC method {@code representatives}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#representatives">Official RPC documentation</a>
 */
public class RequestRepresentatives extends RpcRequest<ResponseRepresentatives> {
    
    @Expose @SerializedName("count")
    private final Integer count;
    
    @Expose @SerializedName("sorting")
    private final Boolean sorting;
    
    
    public RequestRepresentatives() {
        this(null);
    }
    
    /**
     * @param sorting (optional) whether the results should be sorted by voting weight (in descending order)
     */
    public RequestRepresentatives(Boolean sorting) {
        this(sorting, null);
    }
    
    /**
     * @param sorting   (optional) whether the results should be sorted by voting weight (in descending order)
     * @param count     (optional) the limit of representatives to list
     */
    public RequestRepresentatives(Boolean sorting, Integer count) {
        super("representatives", ResponseRepresentatives.class);
        this.sorting = sorting;
        this.count = count;
    }
    
    
    /**
     * @return the requested limit
     */
    public Integer getCount() {
        return count;
    }
    
    /**
     * @return the requested sorting option
     */
    public Boolean getSorting() {
        return sorting;
    }
    
}
