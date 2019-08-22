package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * This response class contains a set of work peers.
 */
public class ResponseWorkPeers extends RpcResponse {
    
    @Expose @SerializedName("work_peers")
    private Set<String> peerAddresses;
    
    
    /**
     * @return a set of remote work peers' IP address and port
     */
    public Set<String> getWorkPeers() {
        return peerAddresses;
    }
    
}
