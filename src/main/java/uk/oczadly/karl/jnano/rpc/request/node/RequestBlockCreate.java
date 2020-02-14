package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.rpc.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCreate;

import java.math.BigInteger;

/**
 * This request class is used to create a new state block. Use the {@link Builder} class to create the block, rather
 * than the constructor.
 * The server responds with a {@link ResponseBlockCreate} data object.<br>
 * Calls the internal RPC method {@code block_create}.
 *
 * @see Builder
 * @see <a href="https://docs.nano.org/commands/rpc-protocol/#block_create">Official RPC documentation</a>
 */
public class RequestBlockCreate extends RpcRequest<ResponseBlockCreate> {
    
    @Expose @SerializedName("json_block")
    private final boolean jsonBlock = true;
    
    @Expose @SerializedName("type")
    private final BlockType type = BlockType.STATE;
    
    @Expose @SerializedName("balance")
    private final BigInteger balance;
    
    @Expose @SerializedName("representative")
    private final String representative;
    
    @Expose @SerializedName("previous")
    private final String previous;
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final String account;
    
    @Expose @SerializedName("key")
    private final String privateKey;
    
    @Expose @SerializedName("source")
    private final String sourceBlock;
    
    @Expose @SerializedName("destination")
    private final String destination;
    
    @Expose @SerializedName("link")
    private final String linkData;
    
    @Expose @SerializedName("work")
    private final String work;
    
    
    private RequestBlockCreate(BigInteger balance, String representative, String previous, String walletId, String account, String privateKey, String sourceBlock, String destination, String linkData, String work) {
        super("block_create", ResponseBlockCreate.class);
        this.balance = balance;
        this.representative = representative;
        this.previous = previous;
        this.walletId = walletId;
        this.account = account;
        this.privateKey = privateKey;
        this.sourceBlock = sourceBlock;
        this.destination = destination;
        this.linkData = linkData;
        this.work = work;
    }
    
    
    /**
     * @return the requested block's balance field
     */
    public BigInteger getBalance() {
        return balance;
    }
    
    /**
     * @return the requested block's representative field
     */
    public String getRepresentative() {
        return representative;
    }
    
    /**
     * @return the requested block's previous field
     */
    public String getPrevious() {
        return previous;
    }
    
    /**
     * @return the requested wallet ID to sign the block
     */
    public String getWalletId() {
        return walletId;
    }
    
    /**
     * @return the requested account to sign the block
     */
    public String getAccount() {
        return account;
    }
    
    /**
     * @return the requested private key to sign the block
     */
    public String getPrivateKey() {
        return privateKey;
    }
    
    /**
     * @return the requested block's source field
     */
    public String getSourceBlock() {
        return sourceBlock;
    }
    
    /**
     * @return the requested block's destination field
     */
    public String getDestination() {
        return destination;
    }
    
    /**
     * @return the requested block's link field
     */
    public String getLinkData() {
        return linkData;
    }
    
    /**
     * @return the requested block's work field
     */
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
            return new RequestBlockCreate(balance, representative, previous, walletId, account, privKey, sourceBlock,
                    destination, link, work);
        }
        
    }
    
}
