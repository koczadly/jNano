package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseUncheckedKeys;

public class RequestUncheckedKeys extends RpcRequest<ResponseUncheckedKeys> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    
    @Expose @SerializedName("key")
    private String key;
    
    @Expose @SerializedName("count")
    private int count;
    
    
    public RequestUncheckedKeys(String key, int count) {
        super("unchecked_keys", ResponseUncheckedKeys.class);
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
