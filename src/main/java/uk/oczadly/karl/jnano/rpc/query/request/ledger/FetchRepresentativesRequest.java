package uk.oczadly.karl.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.request.SortingOrder;
import uk.oczadly.karl.jnano.rpc.query.response.FetchRepresentativesResponse;

public class FetchRepresentativesRequest extends RpcRequest<FetchRepresentativesResponse> {
    
    @Expose
    @SerializedName("count")
    private Integer count;
    
    @Expose
    @SerializedName("sorting")
    private SortingOrder order;
    
    
    public FetchRepresentativesRequest() {
        this(SortingOrder.DEFAULT);
    }
    
    public FetchRepresentativesRequest(SortingOrder order) {
        this(order, null);
    }
    
    public FetchRepresentativesRequest(SortingOrder order, Integer count) {
        super("representatives", FetchRepresentativesResponse.class);
        this.order = order;
        this.count = count;
    }
    
    
    
    public Integer getCount() {
        return count;
    }
    
    public SortingOrder getOrder() {
        return order;
    }
    
}
