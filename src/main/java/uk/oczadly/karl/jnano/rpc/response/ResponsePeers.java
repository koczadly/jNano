package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * This response class contains a set of connected peers and associated information about them.
 */
public class ResponsePeers extends RpcResponse {
    
    @Expose @SerializedName("peers")
    private Map<String, PeerInfo> peers;
    
    
    /**
     * Map follows the structure {@code IP address -> node information}.
     *
     * @return a map of representatives
     */
    public Map<String, PeerInfo> getPeers() {
        return peers;
    }
    
    
    public static class PeerInfo {
        @Expose @SerializedName("protocol_version")
        private int protocolVer;
        
        @Expose @SerializedName("node_id")
        private String nodeId;
        
        @Expose @SerializedName("type")
        private String type;
        
        
        /**
         * @return the protocol version this node is using
         */
        public int getProtocolVer() {
            return protocolVer;
        }
        
        /**
         * @return the unique ID of this node
         */
        public String getNodeId() {
            return nodeId;
        }
        
        /**
         * @return the type of network connection to this node
         */
        public String getType() {
            return type;
        }
    }
    
}
