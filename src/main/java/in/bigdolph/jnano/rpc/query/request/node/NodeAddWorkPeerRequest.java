package in.bigdolph.jnano.rpc.query.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

public class NodeAddWorkPeerRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("address")
    private String peerAddress;
    
    @Expose
    @SerializedName("port")
    private int peerPort;
    
    
    public NodeAddWorkPeerRequest(String peerAddress, int peerPort) {
        super("work_peer_add", RpcResponse.class);
        this.peerAddress = peerAddress;
        this.peerPort = peerPort;
    }
    
    
    public String getPeerAddress() {
        return peerAddress;
    }
    
    public int getPeerPort() {
        return peerPort;
    }
    
}
