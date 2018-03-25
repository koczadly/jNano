package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.AccountPendingBlocksResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

import java.math.BigInteger;

public class AccountPendingBlocksRequest extends RPCRequest<AccountPendingBlocksResponse> {
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    @Expose
    @SerializedName("threshold")
    private BigInteger threshold;
    
    @Expose
    @SerializedName("source")
    private boolean fetchSource = true;
    
    
    public AccountPendingBlocksRequest(String account, int count) {
        this(account, count, null);
    }
    
    public AccountPendingBlocksRequest(String account, int count, BigInteger threshold) {
        super("pending", AccountPendingBlocksResponse.class);
        this.account = account;
        this.count = count;
        this.threshold = threshold;
    }
    
    
    
    public String getAccount() {
        return account;
    }
    
    public int getCount() {
        return count;
    }
    
    public BigInteger getThreshold() {
        return threshold;
    }
    
}
