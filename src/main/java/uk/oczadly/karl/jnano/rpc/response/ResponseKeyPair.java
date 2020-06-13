package uk.oczadly.karl.jnano.rpc.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

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
     * @deprecated use {@link #getAddress()}
     */
    @Deprecated
    public String getPublicKey() {
        return publicKey;
    }
    
    /**
     * @return the account's address
     * @deprecated use {@link #getAddress()}
     */
    @Deprecated
    public String getAccountAddress() {
        return accountAddress;
    }
    
    /**
     * @return an {@link NanoAccount} object representing the account returned
     */
    public NanoAccount getAddress() {
        return NanoAccount.parse(getAccountAddress());
    }
    
}
