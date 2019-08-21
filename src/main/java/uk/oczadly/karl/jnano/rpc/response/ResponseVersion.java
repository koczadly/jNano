package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    
    
    public int getRpcVersion() {
        return rpcVersion;
    }
    
    public int getStoreVersion() {
        return storeVersion;
    }
    
    public int getProtocolVersion() {
        return protocolVersion;
    }
    
    public String getNodeVendor() {
        return nodeVendor;
    }
    
    public String getNetwork() {
        return network;
    }
    
    public String getNetworkId() {
        return networkId;
    }
    
    public String getBuildInfo() {
        return buildInfo;
    }
}
