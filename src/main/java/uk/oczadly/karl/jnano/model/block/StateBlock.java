package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.AccountAddress;

import java.math.BigInteger;

/**
 * <p>Represents a state block, and the associated data.</p>
 * <p>To construct a new state block, use the {@link StateBlockBuilder} provided.</p>
 */
public final class StateBlock extends Block {
    
    @Expose @SerializedName("account")
    private AccountAddress accountAddress;
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private AccountAddress representativeAddress;
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    @Expose @SerializedName("link")
    private String linkData;
    
    @Expose @SerializedName("link_as_account")
    private AccountAddress linkAccount;
    
    @Expose @SerializedName("subtype")
    private StateBlockSubType subType;
    
    
    StateBlock() {
        super(BlockType.STATE);
    }
    
    /**
     * Constructs a new state block.
     *
     * @param jsonRepresentation    the JSON representation if deserialized, or null
     * @param subtype               the block's action subtype
     * @param hash                  the block's hash
     * @param signature             the block verification signature
     * @param workSolution          the computed work solution
     * @param accountAddress        the account's address
     * @param previousBlockHash     the previous block's hash
     * @param representativeAddress the representative address of this account
     * @param balance               the (newly updated) balance, in raw
     * @param linkData              the link data for this transaction
     * @param linkAccount           the link data for this transaction, specified in the account address format
     * @see StateBlockBuilder
     */
    StateBlock(JsonObject jsonRepresentation, StateBlockSubType subtype, String hash, String signature,
               String workSolution, AccountAddress accountAddress, String previousBlockHash,
               AccountAddress representativeAddress, BigInteger balance, String linkData, AccountAddress linkAccount) {
        super(BlockType.STATE, hash, jsonRepresentation, signature, workSolution);
        this.subType = subtype;
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.balance = balance;
        this.linkData = linkData;
        this.linkAccount = linkAccount;
    }
    
    
    public AccountAddress getAccountAddress() {
        return accountAddress;
    }
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public AccountAddress getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public final BigInteger getBalance() {
        return balance;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    public AccountAddress getLinkAsAccount() {
        return linkAccount;
    }
    
    public StateBlockSubType getSubType() {
        return subType;
    }
    
}
