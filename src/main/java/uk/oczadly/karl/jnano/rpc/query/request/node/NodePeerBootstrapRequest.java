package uk.oczadly.karl.jnano.rpc.query.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.RpcResponse;

public class NodePeerBootstrapRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("address")
    private String peerAddress;
    
    @Expose
    @SerializedName("port")
    private int peerPort;
    
    
    public NodePeerBootstrapRequest(String peerAddress, int peerPort) {
        super("bootstrap", RpcResponse.class);
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
