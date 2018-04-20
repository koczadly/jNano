package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusResponse extends RpcResponse {
    
    @Expose
    @SerializedName("status")
    private Status status;
    
    
    
    public Status getStatus() {
        return status;
    }
    
    
    
    public enum Status {
        
        @SerializedName("Ready")
        INIT_READY(true),
        
        @SerializedName("Transaction wallet locked")
        INIT_WALLET_LOCKED(false),
        
        @SerializedName("Unable to find transaction wallet")
        INIT_WALLET_UNAVAILABLE(false),
        
        
        @SerializedName("success")
        PAYMENT_SUCCESS(true),
        
        @SerializedName("nothing")
        PAYMENT_NOT_RECEIVED(false);
        
    
        
        private boolean success;
        
        Status(boolean success) {
            this.success = success;
        }
        
        
        public boolean isSuccessful() {
            return this.success;
        }
        
    }
    
}
