package in.bigdolph.jnano.rpc.query.request.compute.blockcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.model.block.BlockType;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockCreateResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public abstract class BlockCreateRequest extends RPCRequest<BlockCreateResponse> {
    
    @Expose
    @SerializedName("type")
    private final BlockType type;
    
    @Expose
    @SerializedName("key")
    private String privateKey;
    
    @Expose
    @SerializedName("wallet")
    private String walletId;
    
    @Expose
    @SerializedName("account")
    private String account;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    
    public BlockCreateRequest(BlockType type, String workSolution, String privateKey) {
        this(type, workSolution);
        this.privateKey = privateKey;
    }
    
    public BlockCreateRequest(BlockType type, String workSolution, String walletId, String account) {
        this(type, workSolution);
        this.walletId = walletId;
        this.account = account;
    }
    
    protected BlockCreateRequest(BlockType type, String workSolution) {
        super("block_create", BlockCreateResponse.class);
        this.type = type;
        this.workSolution = workSolution;
    }
    
    
    
    public final BlockType getBlockType() {
        return type;
    }
    
    public String getWorkSolution() {
        return workSolution;
    }
    
    public final String getPrivateKey() {
        return privateKey;
    }
    
    public final String getWalletId() {
        return walletId;
    }
    
    public final String getAccount() {
        return account;
    }
    
}
