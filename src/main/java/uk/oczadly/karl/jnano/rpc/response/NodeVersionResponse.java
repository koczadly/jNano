package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeVersionResponse extends RpcResponse {
    
    @Expose
    @SerializedName("rpc_version")
    private int rpcVersion;
    
    @Expose
    @SerializedName("store_version")
    private int storeVersion;
    
    @Expose
    @SerializedName("node_vendor")
    private String nodeVendor;
    
    
    
    public int getRPCVersion() {
        return this.rpcVersion;
    }
    
    public int getStoreVersion() {
        return this.storeVersion;
    }
    
    public String getNodeVendor() {
        return this.nodeVendor;
    }
    
}
