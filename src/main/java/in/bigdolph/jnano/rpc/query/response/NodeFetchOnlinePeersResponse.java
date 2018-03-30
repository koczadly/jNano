package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;

public class NodeFetchOnlinePeersResponse extends RPCResponse {
    
    @Expose
    @SerializedName("peers")
    private Map<String, String> peers;
    
    
    /** Address -> Network version */
    public Map<String, String> getPeers() {
        return peers;
    }
    
    public String getPeerNetworkVersion(String address) {
        return this.peers.get(address.toLowerCase());
    }
    
}
