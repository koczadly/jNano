package uk.oczadly.karl.jnano.rpc.query.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountKeyPairResponse extends RpcResponse {

    @Expose
    @SerializedName("private")
    private String privateKey;
    
    @Expose
    @SerializedName("public")
    private String publicKey;
    
    @Expose
    @SerializedName("account")
    private String accountAddress;
    
    
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public String getAccountAddress() {
        return accountAddress;
    }
    
}
