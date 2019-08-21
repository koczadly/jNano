package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseOnlineRepresentatives;

public class RequestRepresentativesOnline extends RpcRequest<ResponseOnlineRepresentatives> {
    
    @Expose @SerializedName("weight")
    private final boolean weight = true;
    
    
    public RequestRepresentativesOnline() {
        super("representatives_online", ResponseOnlineRepresentatives.class);
    }
    
}
