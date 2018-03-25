package in.bigdolph.jnano.rpc.query.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

import java.math.BigInteger;

public class NodeSetMinimumReceiveRequest extends RPCRequest<RPCResponse> {
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    
    public NodeSetMinimumReceiveRequest(BigInteger amount) {
        super("receive_minimum_set", RPCResponse.class);
        this.amount = amount;
    }
    
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
}
