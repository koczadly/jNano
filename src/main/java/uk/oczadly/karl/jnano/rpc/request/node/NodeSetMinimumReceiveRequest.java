package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

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
