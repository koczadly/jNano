package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlocksMap;

public class RequestUnchecked extends RpcRequest<ResponseBlocksMap> {
    
    @Expose @SerializedName("count")
    private int count;
    
    
    public RequestUnchecked(int count) {
        super("unchecked", ResponseBlocksMap.class);
        this.count = count;
    }
    
    
    
    public int getCount() {
        return count;
    }
    
}
