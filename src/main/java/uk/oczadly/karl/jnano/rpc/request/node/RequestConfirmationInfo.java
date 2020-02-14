package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationInfo;

/**
 * This request class is used to request information about the specified active election.
 * The server responds with a {@link ResponseConfirmationInfo} data object.<br>
 * Calls the internal RPC method {@code confirmation_info}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#confirmation_info">Official RPC documentation</a>
 */
public class RequestConfirmationInfo extends RpcRequest<ResponseConfirmationInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("contents")
    private final boolean contents = true;
    
    @Expose @SerializedName("representatives")
    private final boolean representatives = true;
    
    
    @Expose @SerializedName("root")
    private final String rootHash;
    
    
    /**
     * @param rootHash the election's root hash
     */
    public RequestConfirmationInfo(String rootHash) {
        super("confirmation_info", ResponseConfirmationInfo.class);
        this.rootHash = rootHash;
    }
    
    
    /**
     * @return the requested election's root hash
     */
    public String getRootHash() {
        return rootHash;
    }
    
}
