package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.RpcResponse;

/**
 * This response class contains debug information about the node's ID.
 */
public class ResponseNodeId extends RpcResponse {
    
    @Expose @SerializedName("private")
    private String privateKey;
    
    @Expose @SerializedName("public")
    private String publicKey;
    
    @Expose @SerializedName("as_account")
    private String asAccount;
    
    @Expose @SerializedName("node_id")
    private String nodeId;
    
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public String getPublicKeyAccount() {
        return asAccount;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
}
