package in.bigdolph.jnano.rpc.query.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;

import java.math.BigInteger;

public class StateBlockCreateRequest extends BlockCreateRequest {
    
    @Expose
    @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose
    @SerializedName("representative")
    private String representativeAddress;
    
    @Expose
    @SerializedName("balance")
    private BigInteger newBalance;
    
    @Expose
    @SerializedName("link")
    private String linkData;
    
    
    public StateBlockCreateRequest(String account, String previousBlockHash, String representativeAddress, BigInteger newBalance, String linkData, String privateKey, String workSolution) {
        super(BlockType.STATE, null, account, privateKey, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.newBalance = newBalance;
        this.linkData = linkData;
    }
    
    public StateBlockCreateRequest(String previousBlockHash, String representativeAddress, BigInteger newBalance, String linkData, String walletId, String account, String workSolution) {
        super(BlockType.STATE, walletId, account, workSolution);
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.newBalance = newBalance;
        this.linkData = linkData;
    }
    
    
    
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public String getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public BigInteger getBalance() {
        return newBalance;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
}
