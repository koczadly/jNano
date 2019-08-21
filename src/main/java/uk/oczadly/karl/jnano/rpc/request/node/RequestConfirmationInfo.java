package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseConfirmationInfo;

public class RequestConfirmationInfo extends RpcRequest<ResponseConfirmationInfo> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = true;
    
    @Expose @SerializedName("contents")
    private final boolean contents = true;
    
    @Expose @SerializedName("representatives")
    private final boolean representatives = true;
    
    
    @Expose @SerializedName("root")
    private String rootHash;
    
    
    public RequestConfirmationInfo(String rootHash) {
        super("confirmation_info", ResponseConfirmationInfo.class);
        this.rootHash = rootHash;
    }
    
    
    public String getRootHash() {
        return rootHash;
    }
    
}
