package in.bigdolph.jnano.rpc.query.request.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.generic.BlockHashResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockReceiveRequest extends RPCRequest<BlockHashResponse> {
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("block")
    private String blockHash;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    public BlockReceiveRequest(String walletId, String account, String blockHash) {
        this(walletId, account, blockHash, null);
    }
    
    public BlockReceiveRequest(String walletId, String account, String blockHash, String workSolution) {
        super("receive", BlockHashResponse.class);
        this.walletId = walletId;
        this.account = account;
        this.blockHash = blockHash;
        this.workSolution = workSolution;
    }
    
    
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
    
}
