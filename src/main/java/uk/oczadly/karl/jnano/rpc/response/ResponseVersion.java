package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains details about the node's version.
 */
public class ResponseVersion extends RpcResponse {
    
    @Expose @SerializedName("rpc_version")
    private int rpcVersion;
    
    @Expose @SerializedName("store_version")
    private int storeVersion;
    
    @Expose @SerializedName("protocol_version")
    private int protocolVersion;
    
    @Expose @SerializedName("node_vendor")
    private String nodeVendor;
    
    @Expose @SerializedName("network")
    private String network;
    
    @Expose @SerializedName("network_identifier")
    private String networkId;
    
    @Expose @SerializedName("build_info")
    private String buildInfo;
    
    
    /**
     * @return the RPC version
     */
    public int getRpcVersion() {
        return rpcVersion;
    }
    
    /**
     * @return the store (database) version
     */
    public int getStoreVersion() {
        return storeVersion;
    }
    
    /**
     * @return the protocol (network) version
     */
    public int getProtocolVersion() {
        return protocolVersion;
    }
    
    /**
     * @return the node vendor, or display name
     */
    public String getNodeVendor() {
        return nodeVendor;
    }
    
    /**
     * @return the active network
     */
    public String getNetwork() {
        return network;
    }
    
    /**
     * @return the active network's ID (hash of the genesis block)
     */
    public String getNetworkId() {
        return networkId;
    }
    
    /**
     * @return detailed build information
     */
    public String getBuildInfo() {
        return buildInfo;
    }
}
