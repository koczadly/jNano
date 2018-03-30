package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class NodeVersionResponse extends RPCResponse {
    
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
