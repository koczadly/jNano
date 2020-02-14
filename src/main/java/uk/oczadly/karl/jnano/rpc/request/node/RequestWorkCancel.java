package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to cancel a pending/ongoing work generation task.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code work_cancel}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_cancel">Official RPC documentation</a>
 */
public class RequestWorkCancel extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("hash")
    private final String blockHash;
    
    
    public RequestWorkCancel(String blockHash) {
        super("work_cancel", ResponseSuccessful.class);
        this.blockHash = blockHash;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
