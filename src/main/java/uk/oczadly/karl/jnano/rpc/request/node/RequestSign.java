package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseSignature;

public class RequestSign extends RpcRequest<ResponseSignature> {
    
    @Expose @SerializedName("json_block")
    private final boolean json = false;
    
    
    @Expose @SerializedName("hash")
    private String blockHash;
    
    @Expose @SerializedName("block")
    private String blockJson;
    
    @Expose @SerializedName("key")
    private String privateKey;
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    
    public RequestSign(String blockJson, String privateKey) {
        this(null, blockJson, privateKey, null, null);
    }
    
    public RequestSign(String blockJson, String walletId, String account) {
        this(null, blockJson, null, walletId, account);
    }
    
    public RequestSign(String blockHash) {
        this(blockHash, null, null, null, null);
    }
    
    protected RequestSign(String blockHash, String blockJson, String privateKey, String walletId, String account) {
        super("sign", ResponseSignature.class);
        this.blockHash = blockHash;
        this.blockJson = blockJson;
        this.privateKey = privateKey;
        this.walletId = walletId;
        this.account = account;
    }
    
    
    public String getBlockHash() {
        return blockHash;
    }
    
    public String getBlockJson() {
        return blockJson;
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
}
