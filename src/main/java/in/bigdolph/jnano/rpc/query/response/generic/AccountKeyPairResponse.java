package in.bigdolph.jnano.rpc.query.response.generic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import in.bigdolph.jnano.rpc.query.response.RPCResponse;

public class AccountKeyPairResponse extends RPCResponse {

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
