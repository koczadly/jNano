package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to add a work peer to the node.
 * The server responds with a {@link ResponseSuccessful} data object.<br>
 * Calls the internal RPC method {@code work_peer_add}.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#work_peer_add">Official RPC documentation</a>
 */
public class RequestWorkPeerAdd extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("address")
    private final String peerAddress;
    
    @Expose @SerializedName("port")
    private final int peerPort;
    
    
    /**
     * @param peerAddress   the IP address of the remote work peer
     * @param peerPort      the port of the remote work peer
     */
    public RequestWorkPeerAdd(String peerAddress, int peerPort) {
        super("work_peer_add", ResponseSuccessful.class);
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }
    
    
    /**
     * @return the peer's remote address
     */
    public String getPeerAddress() {
        return peerAddress;
    }
    
    /**
     * @return the peers remote port
     */
    public int getPeerPort() {
        return peerPort;
    }
    
}
