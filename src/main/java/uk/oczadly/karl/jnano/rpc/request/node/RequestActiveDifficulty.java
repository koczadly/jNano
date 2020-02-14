package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseActiveDifficulty;

/**
 * This request class is used to request the current active difficulty.
 * The server responds with a {@link ResponseActiveDifficulty} data object.<br>
 * Calls the internal RPC method {@code active_difficulty}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#active_difficulty">Official RPC documentation</a>
 */
public class RequestActiveDifficulty extends RpcRequest<ResponseActiveDifficulty> {
    
    @Expose @SerializedName("include_trend")
    private final boolean trend = true;
    
    
    public RequestActiveDifficulty() {
        super("active_difficulty", ResponseActiveDifficulty.class);
    }
    
}
