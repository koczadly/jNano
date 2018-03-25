package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.FetchRepresentativesResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class FetchRepresentativesRequest extends RPCRequest<FetchRepresentativesResponse> {
    
    @Expose
    @SerializedName("count")
    private Integer count;
    
    
    public FetchRepresentativesRequest() {
        this(null);
    }
    
    public FetchRepresentativesRequest(Integer count) {
        super("representatives", FetchRepresentativesResponse.class);
        this.count = count;
    }
    
    
    
    public Integer getCount() {
        return count;
    }
    
}
