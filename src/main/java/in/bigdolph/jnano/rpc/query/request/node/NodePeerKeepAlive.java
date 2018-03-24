package in.bigdolph.jnano.rpc.query.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class NodePeerKeepAlive extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("address")
    private String peerAddress;
    
    @Expose
    @SerializedName("port")
    private int peerPort;
    
    
    public NodePeerKeepAlive(String peerAddress, int peerPort) {
        super("keepalive", RPCResponse.class);
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
