package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.math.BigInteger;
import java.util.function.Function;

/**
 * <p>Represents a state block, and the associated data.</p>
 * <p>To construct a new state block, use the {@link StateBlockBuilder} provided.</p>
 */
public final class StateBlock extends Block implements IBlockLink, IBlockBalance, IBlockPrevious, IBlockRepresentative,
        IBlockAccount {
    
    /** A function which converts a {@link JsonObject} into a {@link StateBlock} instance. */
    public static final Function<JsonObject, StateBlock> DESERIALIZER = json -> new StateBlock(
            JNH.nullable(json.get("subtype"), o -> StateBlockSubType.getFromName(o.getAsString())),
            JNH.nullable(json.get("hash"), JsonElement::getAsString),
            json.get("signature").getAsString(),
            JNH.nullable(json.get("work"), o -> new WorkSolution(o.getAsString())),
            NanoAccount.parseAddress(json.has("account") ? json.get("account").getAsString() :
                    json.get("representative").getAsString()),
            json.get("previous").getAsString(),
            NanoAccount.parseAddress(json.get("representative").getAsString()),
            json.get("balance").getAsBigInteger(),
            JNH.nullable(json.get("link"), JsonElement::getAsString),
            JNH.nullable(json.get("link_as_account"), o -> NanoAccount.parseAddress(o.getAsString()))
    );
    
    /** Prefix for block hashing. */
    private static final byte[] HASH_PREAMBLE_BYTES = JNH.leftPadByteArray(new byte[] {6}, 32, false);
    
    
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
                      String previousBlockHash, NanoAccount representativeAddress, BigInteger balance,
                      NanoAccount link) {
        this(subtype, null, signature, work, accountAddress, previousBlockHash, representativeAddress,
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
        this(subtype, null, signature, work, accountAddress, previousBlockHash, representativeAddress,
                balance, link, null);
    }
    
    StateBlock(StateBlockSubType subtype, String hash, String signature,
               WorkSolution work, NanoAccount accountAddress, String previousBlockHash,
               NanoAccount representativeAddress, BigInteger balance, String linkData, NanoAccount linkAccount) {
        super(BlockType.STATE, hash, signature, work);
        
        if (!JNH.isValidHex(previousBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (representativeAddress == null) throw new IllegalArgumentException("Block representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (!JNH.isBalanceValid(balance))
            throw new IllegalArgumentException("Account balance is an invalid amount.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (!JNH.isValidHex(linkData, HASH_LENGTH))
            throw new IllegalArgumentException("Link data is invalid.");
        
        this.subType = subtype;
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash != null ? previousBlockHash.toUpperCase() : JNH.ZEROES_64;
        this.representativeAddress = representativeAddress;
        this.balance = balance;
        if (linkAccount == null && linkData == null) // If no data field is specified
            linkData = JNH.ZEROES_64;
        this.linkData = linkData != null ? linkData.toUpperCase() : linkAccount.toPublicKey();
        this.linkAccount = linkAccount != null ? linkAccount : NanoAccount.parsePublicKey(linkData);
    }
    
    
    /**
     * @return the subtype of the block (may be null)
     */
    public final StateBlockSubType getSubType() {
        return subType;
    }
    
    @Override
    public final NanoAccount getAccount() {
        return accountAddress;
    }
    
    @Override
    public final String getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representativeAddress;
    }
    
    @Override
    public final BigInteger getBalance() {
        return balance;
    }
    
    /**
     * {@inheritDoc}
     * Returns the link data field, as a 64-character hexadecimal string. For blocks which aren't initialized with this
     * field, the value will be computed.
     */
    @Override
    public final String getLinkData() {
        return linkData;
    }
    
    /**
     * {@inheritDoc}
     * Returns the link data field, encoded as a Nano account. For blocks which aren't initialized with this field,
     * the value will be computed.
     */
    @Override
    public final NanoAccount getLinkAsAccount() {
        return linkAccount;
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                HASH_PREAMBLE_BYTES,
                getAccount().getPublicKeyBytes(),
                JNH.ENC_16.decode(getPreviousBlockHash()),
                getRepresentative().getPublicKeyBytes(),
                JNH.leftPadByteArray(getBalance().toByteArray(), 16, false),
                JNH.ENC_16.decode(getLinkData())
        };
    }
    
}
