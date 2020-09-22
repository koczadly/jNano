/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.model.block.interfaces.*;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>Represents a state block, and the associated data.</p>
 * <p>To construct a new state block, use the {@link StateBlockBuilder} provided.</p>
 */
public final class StateBlock extends Block implements IBlockLink, IBlockBalance, IBlockPrevious, IBlockRepresentative,
        IBlockAccount {
    
    /** A function which converts a {@link JsonObject} into a {@link StateBlock} instance. */
    public static final Function<JsonObject, StateBlock> DESERIALIZER = json -> new StateBlock(
            JNH.getJson(json, "subtype", StateBlockSubType::getFromName),
            JNH.getJson(json, "signature"),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "account", NanoAccount::parseAddress),
            JNH.getJson(json, "previous"),
            JNH.getJson(json, "representative", NanoAccount::parseAddress),
            JNH.getJson(json, "balance", NanoAmount::new),
            JNH.getJson(json, "link"),
            JNH.getJson(json, "link_as_account", NanoAccount::parseAddress));
    
    /** Prefix for block hashing. */
    private static final byte[] HASH_PREAMBLE_BYTES = JNH.leftPadByteArray(new byte[] {6}, 32, false);
    
    
    @Expose @SerializedName("account")
    private final NanoAccount accountAddress;
    
    @Expose @SerializedName("previous")
    private final String previousBlockHash;
    
    @Expose @SerializedName("representative")
    private final NanoAccount representativeAddress;
    
    @Expose @SerializedName("balance")
    private final NanoAmount balance;
    
    @Expose @SerializedName("link")
    private final String linkData;
    
    @Expose @SerializedName("link_as_account")
    private final NanoAccount linkAccount;
    
    @Expose @SerializedName("subtype")
    private final StateBlockSubType subType;
    
    
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
                      String previousBlockHash, NanoAccount representativeAddress, NanoAmount balance, String link) {
        this(subtype, signature, work, accountAddress, previousBlockHash, representativeAddress,
                balance, link, null);
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
                      String previousBlockHash, NanoAccount representativeAddress, NanoAmount balance,
                      NanoAccount link) {
        this(subtype, signature, work, accountAddress, previousBlockHash, representativeAddress,
                balance, null, link);
    }
    
    StateBlock(StateBlockSubType subtype, String signature, WorkSolution work, NanoAccount accountAddress,
                      String previousBlockHash, NanoAccount representativeAddress, NanoAmount balance,
                      String linkData, NanoAccount linkAccount) {
        super(BlockType.STATE, signature, work);
        
        if (subtype == null)
            throw new IllegalArgumentException("Subtype cannot be null.");
        if (previousBlockHash == null)
            throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidHex(previousBlockHash, HASH_LENGTH))
            throw new IllegalArgumentException("Previous block hash is invalid.");
        if (representativeAddress == null) throw new IllegalArgumentException("Block representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (accountAddress == null) throw new IllegalArgumentException("Block account cannot be null.");
        if (linkAccount == null && linkData == null) // If no data field is specified
            throw new IllegalArgumentException("Link data/account cannot be null.");
        if (!JNH.isValidHex(linkData, HASH_LENGTH))
            throw new IllegalArgumentException("Link data is invalid.");
        if (linkAccount != null && linkData != null && !linkAccount.toPublicKey().equals(linkData))
            throw new IllegalArgumentException("Both link types were specified, but their values did not match.");
        
        this.subType = subtype;
        this.accountAddress = accountAddress;
        this.previousBlockHash = previousBlockHash.toUpperCase();
        this.representativeAddress = representativeAddress;
        this.balance = balance;
        this.linkData = linkData != null ? linkData.toUpperCase() : linkAccount.toPublicKey();
        this.linkAccount = linkAccount != null ? linkAccount : NanoAccount.parsePublicKey(linkData);
    }
    
    
    /**
     * @return the subtype of the block
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
    public final NanoAmount getBalance() {
        return balance;
    }
    
    @Override
    public final String getLinkData() {
        return linkData;
    }
    
    @Override
    public final NanoAccount getLinkAsAccount() {
        return linkAccount;
    }
    
    @Override
    public LinkType getLinkType() {
        switch (subType) {
            case SEND:
                return LinkType.DESTINATION;
            case RECEIVE:
            case OPEN:
                return LinkType.SOURCE_HASH;
            case EPOCH:
                return LinkType.EPOCH_IDENTIFIER;
            default:
                return LinkType.NOT_USED;
        }
    }
    
    @Override
    public BlockIntent getIntent() {
        boolean isOpen = subType == StateBlockSubType.OPEN || JNH.isZero(previousBlockHash, false);
        boolean isEpoch = subType == StateBlockSubType.EPOCH || (isOpen && balance.equals(NanoAmount.ZERO));
        boolean isSend = subType == StateBlockSubType.SEND;
        boolean isReceive = subType == StateBlockSubType.RECEIVE || (isOpen && !isEpoch);
        
        return new BlockIntent(
                BlockIntent.UncertainBool.valueOf(isSend),
                BlockIntent.UncertainBool.valueOf(isReceive),
                (subType == StateBlockSubType.SEND || subType == StateBlockSubType.RECEIVE) ?
                        BlockIntent.UncertainBool.UNKNOWN :
                        BlockIntent.UncertainBool.valueOf(subType == StateBlockSubType.CHANGE || isOpen),
                BlockIntent.UncertainBool.valueOf(isOpen),
                BlockIntent.UncertainBool.valueOf(isEpoch),
                BlockIntent.UncertainBool.FALSE);
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof StateBlock)) return false;
        StateBlock sb = (StateBlock)block;
        return super.contentEquals(sb)
                && getSubType() == sb.getSubType()
                && Objects.equals(getAccount(), sb.getAccount())
                && Objects.equals(getLinkData(), sb.getLinkData())
                && Objects.equals(getRepresentative(), sb.getRepresentative())
                && Objects.equals(getPreviousBlockHash(), sb.getPreviousBlockHash())
                && Objects.equals(getBalance(), sb.getBalance());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                HASH_PREAMBLE_BYTES,
                getAccount().getPublicKeyBytes(),
                JNH.ENC_16.decode(getPreviousBlockHash()),
                getRepresentative().getPublicKeyBytes(),
                JNH.leftPadByteArray(getBalance().getAsRaw().toByteArray(), 16, false),
                getLinkAsAccount().getPublicKeyBytes()
        };
    }
    
    
    /**
     * Parses a {@code state} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link StateBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static StateBlock parse(String json) {
        return parse(JNH.parseJson(json));
    }
    
    /**
     * Parses a {@code state} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link StateBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static StateBlock parse(JsonObject json) {
        Block b = Block.parse(json);
        try {
            return (StateBlock)b;
        } catch (ClassCastException e) {
            throw new BlockDeserializer.BlockParseException("Block is not a state block.", e);
        }
    }
    
}
