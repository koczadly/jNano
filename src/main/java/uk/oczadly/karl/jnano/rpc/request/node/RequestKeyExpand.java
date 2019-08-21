package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseKeyPair;

public class RequestKeyExpand extends RpcRequest<ResponseKeyPair> {
    
    @Expose @SerializedName("key")
    private String privateKey;
    
    
    public RequestKeyExpand(String privateKey) {
        super("key_expand", ResponseKeyPair.class);
        this.privateKey = privateKey;
    }
    
    
    public String getPrivateKey() {
        return privateKey;
    }
    
}
