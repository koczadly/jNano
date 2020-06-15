package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.math.BigInteger;

/**
 * <p>Represents a state block, and the associated data.</p>
 * <p>To construct a new state block, use the {@link StateBlockBuilder} provided.</p>
 */
public final class StateBlock extends Block {
    
    @Expose @SerializedName("account")
    private NanoAccount accountAddress;
    
    @Expose @SerializedName("previous")
    private String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private NanoAccount representativeAddress;
    
    @Expose @SerializedName("balance")
    private BigInteger balance;
    
    @Expose @SerializedName("link")
    private String linkData;
    
    @Expose @SerializedName("link_as_account")
    private NanoAccount linkAccount;
    
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
               String workSolution, NanoAccount accountAddress, String previousBlockHash,
               NanoAccount representativeAddress, BigInteger balance, String linkData, NanoAccount linkAccount) {
        super(BlockType.STATE, hash, jsonRepresentation, signature, workSolution);
        if (linkAccount == null && linkData == null)
            throw new IllegalArgumentException("State block must have at least 1 link field populated.");
        this.subType = subtype;
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash;
        this.representativeAddress = representativeAddress;
        this.balance = balance;
        this.linkData = linkData != null ? linkData : linkAccount.toPublicKey();
        this.linkAccount = linkAccount != null ? linkAccount : NanoAccount.parsePublicKey(linkData);
    }
    
    
    public NanoAccount getAccountAddress() {
        return accountAddress;
    }
    
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    public NanoAccount getRepresentativeAddress() {
        return representativeAddress;
    }
    
    public final BigInteger getBalance() {
        return balance;
    }
    
    public String getLinkData() {
        return linkData;
    }
    
    public NanoAccount getLinkAsAccount() {
        return linkAccount;
    }
    
    public StateBlockSubType getSubType() {
        return subType;
    }
    
}
