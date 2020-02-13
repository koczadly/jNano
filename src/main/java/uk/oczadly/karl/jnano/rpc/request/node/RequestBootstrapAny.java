package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to initialize bootstrap connection to random peers.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code bootstrap_any}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap_any">Official RPC documentation</a>
 */
public class RequestBootstrapAny extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("force")
    private Boolean forceClose;
    
    public RequestBootstrapAny() {
        this(null);
    }
    
    /**
     * @param forceClose (optional) whether current bootstraps should be force-closed
     */
    public RequestBootstrapAny(Boolean forceClose) {
        super("bootstrap_any", ResponseSuccessful.class);
        this.forceClose = forceClose;
    }
    
    
    /**
     * @return whether current bootstraps should be force-closed
     */
    public Boolean getForceClose() {
        return forceClose;
    }
    
}
