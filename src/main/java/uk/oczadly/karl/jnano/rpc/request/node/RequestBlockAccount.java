package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseAccount;

public class RequestBlockAccount extends RpcRequest<ResponseAccount> {
    
    @Expose
    @SerializedName("hash")
    private String blockHash;
    
    
    public RequestBlockAccount(String blockHash) {
        super("block_account", ResponseAccount.class);
        this.blockHash = blockHash;
    }
    
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
}
