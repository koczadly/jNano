package in.bigdolph.jnano.rpc.query.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

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
    
    
    public SendBlockCreateRequest(String privateKey, String workSolution, String destinationAccount, BigInteger currentBalance, BigInteger amount, String previousBlockHash) {
        super(BlockType.SEND, privateKey, workSolution);
        this.destinationAccount = destinationAccount;
        this.currentBalance = currentBalance;
        this.amount = amount;
        this.previousBlockHash = previousBlockHash;
    }
    
    public SendBlockCreateRequest(String walletId, String account, String workSolution, String destinationAccount, BigInteger currentBalance, BigInteger amount, String previousBlockHash) {
        super(BlockType.SEND, walletId, account, workSolution);
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
