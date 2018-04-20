package uk.oczadly.karl.jnano.rpc.request.compute.sign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockCreateResponse;

public abstract class BlockCreateRequest extends RpcRequest<BlockCreateResponse> {
    
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
    
    
    public BlockCreateRequest(BlockType type, String privateKey, String workSolution) {
        this(type, null, null, privateKey, workSolution);
    }
    
    public BlockCreateRequest(BlockType type, String walletId, String account, String workSolution) {
        this(type, walletId, account, null, workSolution);
    }
    
    BlockCreateRequest(BlockType type, String walletId, String account, String privateKey, String workSolution) {
        super("block_create", BlockCreateResponse.class);
        this.type = type;
        this.workSolution = workSolution;
        this.walletId = walletId;
        this.account = account;
        this.privateKey = privateKey;
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
