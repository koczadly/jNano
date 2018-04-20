package uk.oczadly.karl.jnano.rpc.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.PaymentStatusResponse;

import java.math.BigInteger;

public class PaymentWaitRequest extends RpcRequest<PaymentStatusResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    @Expose
    @SerializedName("timeout")
    private long timeout;
    
    
    public PaymentWaitRequest(String account, BigInteger amount, long timeout) {
        super("payment_wait", PaymentStatusResponse.class);
        this.account = account;
        this.amount = amount;
        this.timeout = timeout;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public long getTimeout() {
        return timeout;
    }
    
}
