package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlockFetchUncheckedKeyResponse;

public class BlockFetchUncheckedKeyRequest extends RpcRequest<BlockFetchUncheckedKeyResponse> {
    
    @Expose
    @SerializedName("key")
    private String key;
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public BlockFetchUncheckedKeyRequest(String key, int count) {
        super("unchecked_keys", BlockFetchUncheckedKeyResponse.class);
        this.key = key;
        this.count = count;
    }
    
    
    
    public String getKey() {
        return key;
    }
    
    public int getCount() {
        return count;
    }
    
}
