package in.bigdolph.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class PaymentStatusResponse extends RPCResponse {

    @Expose
    @SerializedName("status")
    private Status status;
    
    
    
    public Status getStatus() {
        return status;
    }
    
    
    
    public enum Status {
        @SerializedName("Ready")
        INIT_READY,
        
        
        @SerializedName("success")
        PAYMENT_SUCCESS,
        
        @SerializedName("nothing")
        PAYMENT_NOT_RECEIVED;
    }
    
}
