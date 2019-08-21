package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

public class RequestKeepAlive extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("address")
    private String peerAddress;
    
    @Expose @SerializedName("port")
    private int peerPort;
    
    
    public RequestKeepAlive(String peerAddress, int peerPort) {
        super("keepalive", ResponseSuccessful.class);
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
