package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponsePeers;

public class RequestPeers extends RpcRequest<ResponsePeers> {
    
    @Expose @SerializedName("peer_details")
    private final boolean peerDetails = false;
    
    
    public RequestPeers() {
        super("peers", ResponsePeers.class);
    }
    
}
