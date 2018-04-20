package uk.oczadly.karl.jnano.rpc.request.ledger;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.BlocksResponse;

public class BlockFetchUncheckedRequest extends RpcRequest<BlocksResponse> {
    
    @Expose
    @SerializedName("count")
    private int count;
    
    
    public BlockFetchUncheckedRequest(int count) {
        super("unchecked", BlocksResponse.class);
        this.count = count;
    }
    
    
    
    public int getCount() {
        return count;
    }
    
}
