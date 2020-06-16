package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;

/**
 * <p>Represents a state block, and the associated data.</p>
 * <p>To construct a new state block, use the {@link StateBlockBuilder} provided.</p>
 */
public final class StateBlock extends Block implements BlockInterfaces.Link, BlockInterfaces.Balance,
        BlockInterfaces.Previous, BlockInterfaces.Representative, BlockInterfaces.Account {
    
    private static byte[] HASH_PREAMBLE_BYTES = JNanoHelper.ENCODER_HEX.decode(
            "0000000000000000000000000000000000000000000000000000000000000006");
    
    
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
     * @param subtype               the block's subtype
     * @param signature             the block verification signature
     * @param work                  the computed work solution
     * @param accountAddress        the account's address
     * @param previousBlockHash     the previous block's hash
     * @param representativeAddress the representative address of this account
     * @param balance               the balance of the account after this transaction, in raw
     * @param link                  the link data for this transaction, encoded as an account
     * @see StateBlockBuilder
     */
    public StateBlock(StateBlockSubType subtype, String signature, WorkSolution work, NanoAccount accountAddress,
                      String previousBlockHash, NanoAccount representativeAddress, BigInteger balance, NanoAccount link) {
        this(null, subtype, null, signature, work, accountAddress, previousBlockHash, representativeAddress,
                balance, null, link);
    }
    
    /**
     * Constructs a new state block.
     *
     * @param subtype               the block's subtype
     * @param signature             the block verification signature
     * @param work                  the computed work solution
     * @param accountAddress        the account's address
     * @param previousBlockHash     the previous block's hash
     * @param representativeAddress the representative address of this account
     * @param balance               the balance of the account after this transaction, in raw
     * @param link                  the link data for this transaction, encoded as a hexadecimal string
     * @see StateBlockBuilder
     */
    public StateBlock(StateBlockSubType subtype, String signature, WorkSolution work, NanoAccount accountAddress,
                      String previousBlockHash, NanoAccount representativeAddress, BigInteger balance, String link) {
        this(null, subtype, null, signature, work, accountAddress, previousBlockHash, representativeAddress,
                balance, link, null);
    }
    
    StateBlock(JsonObject jsonRepresentation, StateBlockSubType subtype, String hash, String signature,
               WorkSolution work, NanoAccount accountAddress, String previousBlockHash,
               NanoAccount representativeAddress, BigInteger balance, String linkData, NanoAccount linkAccount) {
        super(BlockType.STATE, hash, jsonRepresentation, signature, work);
        
        if (previousBlockHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNanoHelper.isValidHex(previousBlockHash, 64))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (representativeAddress == null) throw new IllegalArgumentException("Block representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (!JNanoHelper.isBalanceValid(balance))
            throw new IllegalArgumentException("Account balance is an invalid amount.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (linkData != null && !JNanoHelper.isValidHex(linkData, 64))
            throw new IllegalArgumentException("Link data is invalid.");
        
        this.subType = subtype;
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash != null ? previousBlockHash.toUpperCase() : JNanoHelper.EMPTY_HEX_64;
        this.representativeAddress = representativeAddress;
        this.balance = balance;
        if (linkAccount == null && linkData == null) // If no data field is specified
            linkData = JNanoHelper.EMPTY_HEX_64;
        this.linkData = linkData != null ? linkData.toUpperCase() : linkAccount.toPublicKey();
        this.linkAccount = linkAccount != null ? linkAccount : NanoAccount.parsePublicKey(linkData);
    }
    
    
    /**
     * @return the subtype of the block (may be null)
     */
    public StateBlockSubType getSubType() {
        return subType;
    }
    
    @Override
    public NanoAccount getAccount() {
        return accountAddress;
    }
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public NanoAccount getRepresentative() {
        return representativeAddress;
    }
    
    @Override
    public final BigInteger getBalance() {
        return balance;
    }
    
    /**
     * Returns the link data field, as a 64-character hexadecimal string. For blocks which aren't initialized with this
     * field, the value will be computed.
     * @return the link data, encoded as a hexadecimal string
     */
    @Override
    public String getLinkData() {
        return linkData;
    }
    
    /**
     * Returns the link data field, encoded as a Nano account. For blocks which aren't initialized with this field,
     * the value will be computed.
     * @return the link data, encoded as a Nano account
     */
    @Override
    public NanoAccount getLinkAsAccount() {
        return linkAccount;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                HASH_PREAMBLE_BYTES,
                getAccount().getPublicKeyBytes(),
                JNanoHelper.ENCODER_HEX.decode(getPreviousBlockHash()),
                getRepresentative().getPublicKeyBytes(),
                JNanoHelper.padByteArray(getBalance().toByteArray(), 16),
                JNanoHelper.ENCODER_HEX.decode(getLinkData())
        };
    }
    
}
