package uk.oczadly.karl.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.query.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.query.response.PaymentStatusResponse;

public class PaymentInitializeRequest extends RpcRequest<PaymentStatusResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    
    public PaymentInitializeRequest(String walletId) {
        super("payment_init", PaymentStatusResponse.class);
        this.walletId = walletId;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
}
