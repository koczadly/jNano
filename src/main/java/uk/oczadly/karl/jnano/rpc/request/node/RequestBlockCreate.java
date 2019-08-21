package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCreate;

import java.math.BigInteger;

/**
 * @see Builder
 */
public class RequestBlockCreate extends RpcRequest<ResponseBlockCreate> {
    
    @Expose @SerializedName("json_block")
    private final boolean jsonBlock = true;
    
    @Expose @SerializedName("type")
    private final BlockType type = BlockType.STATE;
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    @Expose @SerializedName("representative")
    private String representative;
    
    @Expose @SerializedName("previous")
    private String previous;
    
    @Expose @SerializedName("wallet")
    private String walletId;
    
    @Expose @SerializedName("account")
    private String account;
    
    @Expose @SerializedName("key")
    private String privateKey;
    
    @Expose @SerializedName("source")
    private String sourceBlock;
    
    @Expose @SerializedName("destination")
    private String destination;
    
    @Expose @SerializedName("link")
    private String linkData;
    
    @Expose @SerializedName("work")
    private String work;
    
    
    private RequestBlockCreate() {
        super("block_create", ResponseBlockCreate.class);
    }
    
    
    public BigInteger getBalance() {
        return balance;
    }
    
    public String getRepresentative() {
        return representative;
    }
    
    public String getPrevious() {
        return previous;
    }
    
    public String getWalletId() {
        return walletId;
    }
    
    public String getAccount() {
        return account;
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getSourceBlock() {
        return sourceBlock;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    public String getWork() {
        return work;
    }
    
    
    
    public static class Builder {
        private BigInteger balance;
        private String representative;
        private String previous;
        private String walletId;
        private String account;
        private String privKey;
        private String sourceBlock;
        private String destination;
        private String link;
        private String work;
        
        public Builder(BigInteger balance, String representative, String previous) {
            this.balance = balance;
            this.representative = representative;
            this.previous = previous;
        }
    
        public BigInteger getBalance() {
            return balance;
        }
    
        public Builder setBalance(BigInteger balance) {
            this.balance = balance;
            return this;
        }
    
        public String getRepresentative() {
            return representative;
        }
    
        public Builder setRepresentative(String representative) {
            this.representative = representative;
            return this;
        }
    
        public String getPrevious() {
            return previous;
        }
    
        public Builder setPrevious(String previous) {
            this.previous = previous;
            return this;
        }
    
        public String getWalletId() {
            return walletId;
        }
    
        public Builder setWalletId(String walletId) {
            this.walletId = walletId;
            return this;
        }
    
        public String getAccount() {
            return account;
        }
    
        public Builder setAccount(String account) {
            this.account = account;
            return this;
        }
    
        public String getPrivateKey() {
            return privKey;
        }
    
        public Builder setPrivateKey(String privKey) {
            this.privKey = privKey;
            return this;
        }
    
        public String getSourceBlock() {
            return sourceBlock;
        }
    
        public Builder setSourceBlock(String hash) {
            this.sourceBlock = hash;
            return this;
        }
    
        public String getDestination() {
            return destination;
        }
    
        public Builder setDestination(String destination) {
            this.destination = destination;
            return this;
        }
    
        public String getLink() {
            return link;
        }
    
        public Builder setLink(String link) {
            this.link = link;
            return this;
        }
    
        public String getWork() {
            return work;
        }
    
        public Builder setWork(String work) {
            this.work = work;
            return this;
        }
        
        
        public RequestBlockCreate buildRequest() {
            RequestBlockCreate request = new RequestBlockCreate();
            request.balance = balance;
            request.representative = representative;
            request.previous = previous;
            request.walletId = walletId;
            request.account = account;
            request.privateKey = privKey;
            request.sourceBlock = sourceBlock;
            request.destination = destination;
            request.linkData = link;
            request.work = work;
            return request;
        }
        
    }
    
}
