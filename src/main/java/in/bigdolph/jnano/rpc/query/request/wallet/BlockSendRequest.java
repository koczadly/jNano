package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.BlockHashResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

import java.math.BigInteger;

public class BlockSendRequest extends RPCRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("source")
    private String sourceAccount;
    
    @Expose
    @SerializedName("destination")
    private String destinationAccount;
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    @Expose
    @SerializedName("id")
    private String uniqueId;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    /**
     * @deprecated unique ID should be specified for idempotency
     */
    @Deprecated
    public BlockSendRequest(String walletId, String sourceAccount, String destinationAccount, BigInteger amount) {
        this(walletId, sourceAccount, destinationAccount, amount, null);
    }
    
    public BlockSendRequest(String walletId, String sourceAccount, String destinationAccount, BigInteger amount, String uniqueId) {
        this(walletId, sourceAccount, destinationAccount, amount, uniqueId, null);
    }
    
    public BlockSendRequest(String walletId, String sourceAccount, String destinationAccount, BigInteger amount, String uniqueId, String workSolution) {
        super("send", BlockHashResponse.class);
        this.walletId = walletId;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.uniqueId = uniqueId;
        this.workSolution = workSolution;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getSourceAccount() {
        return sourceAccount;
    }
    
    public String getDestinationAccount() {
        return destinationAccount;
    }
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
    
}
