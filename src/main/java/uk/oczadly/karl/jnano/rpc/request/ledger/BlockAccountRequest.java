package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.AccountResponse;

public class BlockAccountRequest extends RpcRequest<AccountResponse> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public BlockAccountRequest(String blockHash) {
        super("block_account", AccountResponse.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
