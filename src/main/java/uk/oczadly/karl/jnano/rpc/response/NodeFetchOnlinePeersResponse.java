package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class NodeFetchOnlinePeersResponse extends RpcResponse {
    
    @Expose
    @SerializedName("peers")
    private Map<String, String> peers;
    
    
    /** Address -&gt; Network version */
    public Map<String, String> getPeers() {
        return peers;
    }
    
    public String getPeerNetworkVersion(String address) {
        return this.peers.get(address.toLowerCase());
    }
    
}
