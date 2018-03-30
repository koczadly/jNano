package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.AccountsPendingResponse;

import java.math.BigInteger;

public class AccountsPendingRequest extends RPCRequest<AccountsPendingResponse> {
    
    @Expose
    @SerializedName("accounts")
    private String[] accounts;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    @Expose
    @SerializedName("threshold")
    private BigInteger threshold;
    
    @Expose
    @SerializedName("source")
    private boolean source = true;
    
    
    public AccountsPendingRequest(String[] accounts, int count) {
        this(accounts, count, BigInteger.ZERO);
    }
    
    public AccountsPendingRequest(String[] accounts, int count, BigInteger threshold) {
        super("accounts_pending", AccountsPendingResponse.class);
        this.accounts = accounts;
        this.count = count;
        this.threshold = threshold;
    }
    
    
    
    public String[] getAccounts() {
        return accounts;
    }
    
    public int getCount() {
        return count;
    }
    
    public BigInteger getThreshold() {
        return threshold;
    }
    
}
