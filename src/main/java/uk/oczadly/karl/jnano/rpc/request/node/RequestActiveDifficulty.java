package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseActiveDifficulty;

public class RequestActiveDifficulty extends RpcRequest<ResponseActiveDifficulty> {
    
    @Expose @SerializedName("include_trend")
    private boolean trend = true;
    
    
    public RequestActiveDifficulty(String account) {
        super("active_difficulty", ResponseActiveDifficulty.class);
    }
    
}
