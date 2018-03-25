package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.PaymentStatusResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

import java.math.BigInteger;

public class PaymentWaitRequest extends RPCRequest<PaymentStatusResponse> {
    
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
