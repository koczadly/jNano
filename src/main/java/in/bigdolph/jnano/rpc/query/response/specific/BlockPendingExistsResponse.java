package in.bigdolph.jnano.rpc.query.response.specific;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class BlockPendingExistsResponse extends RPCResponse {

    @Expose
    @SerializedName("exists")
    private boolean exists;
    
    
    public boolean doesExist() {
        return exists;
    }
    
}
