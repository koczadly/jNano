package in.bigdolph.jnano.rpc.query.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public abstract class RPCRequest<R extends RPCResponse> {
    
    @Expose
    @SerializedName("action")
    private String actionCommand;
    
    private Class<R> responseClass;
    
    
    public RPCRequest(String actionCommand, Class<R> responseClass) {
        this.actionCommand = actionCommand;
        this.responseClass = responseClass;
    }
    
    
    
    public final String getActionCommand() {
        return this.actionCommand;
    }
    
    public final Class<R> getResponseClass() {
        return this.responseClass;
    }
    
}
