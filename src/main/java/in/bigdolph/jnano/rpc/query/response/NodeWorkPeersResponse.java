package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class NodeWorkPeersResponse extends RPCResponse {
    
    @Expose
    @SerializedName("work_peers")
    private Set<String> peerAddresses;
    
    
    public Set<String> getWorkPeers() {
        return peerAddresses;
    }
    
}
