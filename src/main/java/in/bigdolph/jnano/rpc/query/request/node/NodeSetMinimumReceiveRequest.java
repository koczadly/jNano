package in.bigdolph.jnano.rpc.query.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RpcRequest;
import in.bigdolph.jnano.rpc.query.response.RpcResponse;

import java.math.BigInteger;

public class NodeSetMinimumReceiveRequest extends RpcRequest<RpcResponse> {
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    
    public NodeSetMinimumReceiveRequest(BigInteger amount) {
        super("receive_minimum_set", RpcResponse.class);
        this.amount = amount;
    }
    
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
}
