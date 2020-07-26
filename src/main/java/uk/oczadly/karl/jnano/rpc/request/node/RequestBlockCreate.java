package uk.oczadly.karl.jnano.rpc.request.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.rpc.request.RpcRequest;
import uk.oczadly.karl.jnano.rpc.response.ResponseBlockCreate;

import java.math.BigInteger;

/**
 * This request class is used to create a new state block. Use the {@link Builder} class to construct the request.<br>
 * <br>Calls the RPC command {@code block_create}, and returns a {@link ResponseBlockCreate} data object.
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
    private final NanoAccount representative;
    
    @Expose @SerializedName("previous")
    private final String previous;
    
    @Expose @SerializedName("wallet")
    private final String walletId;
    
    @Expose @SerializedName("account")
    private final NanoAccount account;
    
    @Expose @SerializedName("key")
    private final String privateKey;
    
    @Expose @SerializedName("source")
    private final String sourceBlock;
    
    @Expose @SerializedName("destination")
    private final NanoAccount destination;
    
    @Expose @SerializedName("link")
    private final String linkData;
    
    @Expose @SerializedName("work")
    private final String work;
    
    @Expose @SerializedName("difficulty")
    private final WorkDifficulty workDifficulty;
    
    
    private RequestBlockCreate(BigInteger balance, NanoAccount representative, String previous, String walletId,
                               NanoAccount account, String privateKey, String sourceBlock,
                               NanoAccount destination, String linkData, String work,
                               WorkDifficulty workDifficulty) {
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
        this.workDifficulty = workDifficulty;
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
    public NanoAccount getRepresentative() {
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
    public NanoAccount getAccount() {
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
    public NanoAccount getDestination() {
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
    
    /**
     * @return the requested generated work difficulty
     */
    public WorkDifficulty getWorkDifficulty() {
        return workDifficulty;
    }
    
    
    public static class Builder {
        private BigInteger balance;
        private NanoAccount representative;
        private String previous;
        private String walletId;
        private NanoAccount account;
        private String privKey;
        private String sourceBlock;
        private NanoAccount destination;
        private String link;
        private String work;
        private WorkDifficulty workDifficulty;
        
        /**
         * Sets the parameters from the block data. Note that the private key and wallet parameters will not be
         * assigned.
         *
         * @param block the state block
         */
        public Builder(StateBlock block) {
            this.balance = block.getBalance();
            this.representative = block.getRepresentative();
            this.previous = block.getPreviousBlockHash();
            this.account = block.getAccount();
            if (block.getSubType() == StateBlockSubType.SEND) {
                this.destination = block.getLinkAsAccount();
            } else if (block.getSubType() == StateBlockSubType.RECEIVE) {
                this.sourceBlock = block.getLinkData();
            } else {
                this.link = block.getLinkData();
            }
            this.work = block.getWorkSolution().getAsHexadecimal();
        }
    
        public Builder() {}
        
        /**
         * @param balance        the account's balance after the block
         * @param representative the representative address
         * @param previous       the previous block's hash
         */
        public Builder(BigInteger balance, NanoAccount representative, String previous) {
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
        
        public NanoAccount getRepresentative() {
            return representative;
        }
        
        public Builder setRepresentative(NanoAccount representative) {
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
        
        public NanoAccount getAccount() {
            return account;
        }
        
        public Builder setAccount(NanoAccount account) {
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
        
        public NanoAccount getDestination() {
            return destination;
        }
        
        public Builder setDestination(NanoAccount destination) {
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
    
        public WorkDifficulty getWorkDifficulty() {
            return workDifficulty;
        }
    
        public Builder setWorkDifficulty(WorkDifficulty workDifficulty) {
            this.workDifficulty = workDifficulty;
            return this;
        }
        
    
        public RequestBlockCreate buildRequest() {
            if (link != null && (sourceBlock != null || destination != null)
                    || sourceBlock != null && destination != null)
                throw new IllegalStateException("Only one of the link/source/destination arguments may be set.");
            if (privKey != null && walletId != null)
                throw new IllegalStateException("Private key and wallet ID cannot be assigned at the same time.");
            if (work != null && workDifficulty != null)
                throw new IllegalStateException("Work and work difficulty values cannot both be assigned.");
            if (balance == null)
                throw new IllegalStateException("No balance argument is assigned.");
            if (representative == null)
                throw new IllegalStateException("No representative argument is assigned.");
            if (previous == null)
                throw new IllegalStateException("No previous argument is assigned.");
            if (link == null && sourceBlock == null && destination == null)
                throw new IllegalStateException("No link, source block or destination address is assigned.");
            
            return new RequestBlockCreate(balance, representative, previous, walletId,
                    (privKey == null) ? account : null,
                    privKey, sourceBlock, destination, link, work, workDifficulty);
        }
        
    }
    
}
