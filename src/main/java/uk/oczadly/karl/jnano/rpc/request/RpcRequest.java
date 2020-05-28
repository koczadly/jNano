package uk.oczadly.karl.jnano.rpc.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.response.RpcResponse;

/**
 * <p>This class represents an RPC request, enclosing the request's name, response class and parameters.</p>
 * <p>Classes which extend this class need to specify parameters as private fields, and MUST be marked with Gson's
 * {@link Expose} annotation. The parameter name will be derived from the name of the field, unless specified otherwise
 * using the {@link SerializedName} annotation.</p>
 *
 * @param <R> the expected response data class
 */
public class RpcRequest<R extends RpcResponse> {
    
    @Expose @SerializedName("action")
    private final String actionCommand;
    
    private final transient Class<R> responseClass;
    
    
    /**
     * @param actionCommand the command (or "action") name to be sent to the node
     * @param responseClass the data class to deserialize the response into
     */
    public RpcRequest(String actionCommand, Class<R> responseClass) {
        this.actionCommand = actionCommand.toLowerCase();
        this.responseClass = responseClass;
    }
    
    
    /**
     * @return the official RPC protocol command
     */
    public final String getActionCommand() {
        return this.actionCommand;
    }
    
    /**
     * @return the expected response data class
     */
    public final Class<R> getResponseClass() {
        return this.responseClass;
    }
    
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "command='" + actionCommand + '\'' +
                ", responseClass=" + responseClass.getName() +
                '}';
    }
    
}
