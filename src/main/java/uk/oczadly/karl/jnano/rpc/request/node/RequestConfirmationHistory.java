package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationHistory;

/**
 * This request class is used to request confirmation history and statistics.
 * The server responds with a {@link ResponseConfirmationHistory} data object.<br>
 * Calls the internal RPC method {@code confirmation_history}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#confirmation_history">Official RPC documentation</a>
 */
public class RequestConfirmationHistory extends RpcRequest<ResponseConfirmationHistory> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    public RequestConfirmationHistory() {
        this(null);
    }
    
    /**
     * @param blockHash (optional) the block's hash
     */
    public RequestConfirmationHistory(String blockHash) {
        super("confirmation_history", ResponseConfirmationHistory.class);
        this.blockHash = blockHash;
    }
    
    
    /**
     * @return the requested block's hash
     */
    public String getBlockHash() {
        return blockHash;
    }
    
}
