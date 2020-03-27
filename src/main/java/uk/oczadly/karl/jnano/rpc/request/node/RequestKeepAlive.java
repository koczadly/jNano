package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to tell the node to send a keepalive packet to the specified remote node.
 * <br>Calls the RPC command {@code keepalive}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#keepalive">Official RPC documentation</a>
 */
public class RequestKeepAlive extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("address")
    private final String peerAddress;
    
    @Expose @SerializedName("port")
    private final int peerPort;
    
    
    /**
     * @param peerAddress the remote peer's IP address
     * @param peerPort    the remote peer's port
     */
    public RequestKeepAlive(String peerAddress, int peerPort) {
        super("keepalive", ResponseSuccessful.class);
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }
    
    
    /**
     * @return the requested peer's address
     */
    public String getPeerAddress() {
        return peerAddress;
    }
    
    /**
     * @return the requested peer's port
     */
    public int getPeerPort() {
        return peerPort;
    }
    
}
