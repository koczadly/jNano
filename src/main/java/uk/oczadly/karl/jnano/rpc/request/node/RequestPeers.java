package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponsePeers;

/**
 * This request class is used to fetch a list of online network peers, and their node's information.
 * <br>Calls the RPC command {@code peers}, and returns a {@link ResponsePeers} data object.
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
