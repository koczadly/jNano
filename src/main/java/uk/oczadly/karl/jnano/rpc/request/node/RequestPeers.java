package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponsePeers;

/**
 * This request class is used to fetch a list of online network peers, and their node's information.
 * The server responds with a {@link ResponsePeers} data object.<br>
 * Calls the internal RPC method {@code peers}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#peers">Official RPC documentation</a>
 */
public class RequestPeers extends RpcRequest<ResponsePeers> {
    
    @Expose @SerializedName("peer_details")
    private final boolean peerDetails = false;
    
    
    public RequestPeers() {
        super("peers", ResponsePeers.class);
    }
    
}
