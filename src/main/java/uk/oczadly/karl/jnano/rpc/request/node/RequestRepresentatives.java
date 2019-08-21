package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseRepresentatives;

public class RequestRepresentatives extends RpcRequest<ResponseRepresentatives> {
    
    @Expose @SerializedName("count")
    private Integer count;
    
    @Expose @SerializedName("sorting")
    private Boolean sorting;
    
    
    public RequestRepresentatives(Boolean sorting) {
        this(sorting, null);
    }
    
    public RequestRepresentatives(Boolean sorting, Integer count) {
        super("representatives", ResponseRepresentatives.class);
        this.sorting = sorting;
        this.count = count;
    }
    
    
    
    public Integer getCount() {
        return count;
    }
    
    public Boolean getSorting() {
        return sorting;
    }
    
}
