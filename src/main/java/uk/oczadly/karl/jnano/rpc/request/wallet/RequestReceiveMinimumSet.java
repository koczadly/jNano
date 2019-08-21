package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSuccessful;

import java.math.BigInteger;

public class RequestReceiveMinimumSet extends RpcRequest<ResponseSuccessful> {
    
    @Expose @SerializedName("amount")
    private BigInteger amount;
    
    
    public RequestReceiveMinimumSet(BigInteger amount) {
        super("receive_minimum_set", ResponseSuccessful.class);
        this.amount = amount;
    }
    
    
    public BigInteger getAmount() {
        return amount;
    }
    
}
