package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class ResponseWorkPeers extends RpcResponse {
    
    @Expose @SerializedName("work_peers")
    private Set<String> peerAddresses;
    
    
    public Set<String> getWorkPeers() {
        return peerAddresses;
    }
    
}
