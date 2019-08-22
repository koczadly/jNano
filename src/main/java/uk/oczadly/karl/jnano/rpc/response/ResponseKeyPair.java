package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This response class contains information about an account's keys.
 */
public class ResponseKeyPair extends RpcResponse {

    @Expose @SerializedName("private")
    private String privateKey;
    
    @Expose @SerializedName("public")
    private String publicKey;
    
    @Expose @SerializedName("account")
    private String accountAddress;
    
    
    /**
     * @return the private key
     */
    public String getPrivateKey() {
        return privateKey;
    }
    
    /**
     * @return the public key
     */
    public String getPublicKey() {
        return publicKey;
    }
    
    /**
     * @return the account's address
     */
    public String getAccountAddress() {
        return accountAddress;
    }
    
}
