package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlocksMapResponse;

public class BlockFetchUncheckedRequest extends RpcRequest<BlocksMapResponse> {
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public BlockFetchUncheckedRequest(int count) {
        super("unchecked", BlocksMapResponse.class);
        this.count = count;
    }
    
    
    
    public int getCount() {
        return count;
    }
    
}
