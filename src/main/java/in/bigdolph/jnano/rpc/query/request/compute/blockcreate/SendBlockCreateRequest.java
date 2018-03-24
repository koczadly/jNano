package in.bigdolph.jnano.rpc.query.request.compute.blockcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.request.compute.blockcreate.BlockCreateRequest;

import java.math.BigInteger;

public class SendBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("destination")
    private String destinationAccount;
    
    @Expose
    @SerializedName("balance")
    private BigInteger currentBalance;
    
    @Expose
    @SerializedName("amount")
    private BigInteger amount;
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    
    public SendBlockCreateRequest(String workSolution, String privateKey, String destinationAccount, BigInteger currentBalance, BigInteger amount, String previousBlockHash) {
        super(BlockType.SEND, workSolution, privateKey);
        this.destinationAccount = destinationAccount;
        this.currentBalance = currentBalance;
        this.amount = amount;
        this.previousBlockHash = previousBlockHash;
    }
    
    public SendBlockCreateRequest(String workSolution, String walletId, String account, String destinationAccount, BigInteger currentBalance, BigInteger amount, String previousBlockHash) {
        super(BlockType.SEND, workSolution, walletId, account);
        this.destinationAccount = destinationAccount;
        this.currentBalance = currentBalance;
        this.amount = amount;
        this.previousBlockHash = previousBlockHash;
    }
    
    
    
    public String getDestinationAccount() {
        return destinationAccount;
    }
    
    public BigInteger getCurrentBalance() {
        return currentBalance;
    }
    
    public BigInteger getAmount() {
        return amount;
    }
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
}
