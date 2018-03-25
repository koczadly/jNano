package in.bigdolph.jnano.rpc.query.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.request.RPCRequest;
import in.bigdolph.jnano.rpc.query.response.specific.BlockFetchUncheckedKeyResponse;
import in.bigdolph.jnano.rpc.query.response.specific.NodeVersionResponse;

public class BlockFetchUncheckedKeyRequest extends RPCRequest<BlockFetchUncheckedKeyResponse> {
    
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
