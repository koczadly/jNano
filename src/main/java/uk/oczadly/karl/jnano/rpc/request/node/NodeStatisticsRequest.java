package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.NodeStatisticsResponse;

public class NodeStatisticsRequest extends RpcRequest<NodeStatisticsResponse> {
    
    @Expose
    @SerializedName("type")
    private StatType type;
    
    
    public NodeStatisticsRequest(StatType type) {
        super("stats", NodeStatisticsResponse.class);
        this.type = type;
    }
    
    
    public StatType getType() {
        return type;
    }
    
    
    
    public enum StatType {
        @SerializedName("counters") COUNTER,
        @SerializedName("samples")  SAMPLE;
    }
    
}
