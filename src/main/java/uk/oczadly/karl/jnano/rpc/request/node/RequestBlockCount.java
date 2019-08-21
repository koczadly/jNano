package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCount;

public class RequestBlockCount extends RpcRequest<ResponseBlockCount> {
    
    @Expose
    @SerializedName("include_cemented")
    private boolean includeCemented;
    
    
    public RequestBlockCount() {
        this(null);
    }
    
    public RequestBlockCount(Boolean includeCemented) {
        super("block_count", ResponseBlockCount.class);
        this.includeCemented = includeCemented;
    }
    
    
    public boolean getIncludeCemented() {
        return includeCemented;
    }
    
}
