package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

/**
 * This request class is used to initialize bootstrap to a specified remote node.
 * <br>Calls the RPC command {@code bootstrap}, and returns a {@link ResponseSuccessful} data object.
 *
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#bootstrap">Official RPC documentation</a>
 */
public class RequestBootstrap extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("address")
    private final String peerAddress;
    
    @Expose @SerializedName("port")
    private final int peerPort;
    
    @Expose @SerializedName("bypass_frontier_confirmation")
    private Boolean bypassFrontierConfirmation;
    
    
    /**
     * @param peerAddress   the IP address of the remote node
     * @param peerPort      the port of the remote node
     */
    public RequestBootstrap(String peerAddress, int peerPort) {
        this(peerAddress, peerPort, null);
    }
    
    /**
     * @param peerAddress   the IP address of the remote node
     * @param peerPort      the port of the remote node
     * @param bypassFrontierConfirmation (optional) whether frontier confirmation should not be performed
     */
    public RequestBootstrap(String peerAddress, int peerPort, Boolean bypassFrontierConfirmation) {
        super("bootstrap", ResponseSuccessful.class);
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
        this.bypassFrontierConfirmation = bypassFrontierConfirmation;
    }
    
    
    /**
     * @return the requested node's IP address
     */
    public String getPeerAddress() {
        return peerAddress;
    }
    
    /**
     * @return the requested node's port
     */
    public int getPeerPort() {
        return peerPort;
    }
    
    /**
     * @return whether frontier confirmation should not be performed
     */
    public Boolean getBypassFrontierConfirmation() {
        return bypassFrontierConfirmation;
    }
}
