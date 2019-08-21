package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ResponsePeers extends RpcResponse {
    
    @Expose @SerializedName("peers")
    private Map<String, PeerInfo> peers;
    
    
    public Map<String, PeerInfo> getPeers() {
        return peers;
    }
    
    public PeerInfo getPeerInfo(String address) {
        return this.peers.get(address.toLowerCase());
    }
    
    
    
    public static class PeerInfo {
        @Expose @SerializedName("protocol_version")
        private int protocolVer;
    
        @Expose @SerializedName("node_id")
        private String nodeId;
    
        @Expose @SerializedName("type")
        private String type;
        
        
        public int getProtocolVer() {
            return protocolVer;
        }
    
        public String getNodeId() {
            return nodeId;
        }
    
        public String getType() {
            return type;
        }
    }
    
}
