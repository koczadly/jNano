package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.PaymentStatusResponse;

public class PaymentInitializeRequest extends RPCRequest<PaymentStatusResponse> {
    
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
