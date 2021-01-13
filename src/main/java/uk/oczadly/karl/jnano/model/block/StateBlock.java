/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
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
            JNH.getJson(json, "signature", HexData::new),
            JNH.getJson(json, "work", WorkSolution::new),
            JNH.getJson(json, "account", NanoAccount::parseAddress),
            JNH.getJson(json, "previous", HexData::new),
            JNH.getJson(json, "representative", NanoAccount::parseAddress),
            JNH.getJson(json, "balance", NanoAmount::valueOfRaw),
            JNH.getJson(json, "link", HexData::new),
            JNH.getJson(json, "link_as_account", NanoAccount::parseAddress));
    
    /** Prefix for block hashing. */
    private static final byte[] HASH_PREAMBLE = JNH.leftPadByteArray(new byte[] {6}, 32, false);
    
    
    @Expose @SerializedName("account")
    private final NanoAccount account;
    
    @Expose @SerializedName("previous")
    private final HexData prevHash;
    
    @Expose @SerializedName("representative")
    private final NanoAccount repAddr;
    
    @Expose @SerializedName("balance")
    private final NanoAmount balance;
    
    @Expose @SerializedName("subtype")
    private final StateBlockSubType subtype;
    
    private final transient LinkData link;
    
    
    /**
     * Constructs a new state block.
     *
     * @param subtype   the block's subtype
     * @param signature the block verification signature
     * @param work      the computed work solution
     * @param account   the account's address
     * @param prevHash  the previous block's hash
     * @param repAddr   the representative address of this account
     * @param balance   the balance of the account after this transaction, in raw
     * @param link      the link data for this transaction, encoded as a hexadecimal string
     *
     * @deprecated {@link StateBlockBuilder} builder class should be used instead
     */
    @Deprecated(forRemoval = true)
    public StateBlock(StateBlockSubType subtype, String signature, WorkSolution work, NanoAccount account,
                      String prevHash, NanoAccount repAddr, NanoAmount balance, String link) {
        this(subtype, new HexData(signature), work, account, new HexData(prevHash),
                repAddr, balance, parseLinkData(subtype, new HexData(link, NanoConst.LEN_HASH_B), null));
    }
    
    /**
     * Constructs a new state block.
     *
     * @param subtype    the block's subtype
     * @param signature  the block verification signature
     * @param work       the computed work solution
     * @param account    the account's address
     * @param prevHash   the previous block's hash
     * @param repAddr    the representative address of this account
     * @param balance    the balance of the account after this transaction, in raw
     * @param link       the link data for this transaction, encoded as an account
     *
     * @deprecated {@link StateBlockBuilder} builder class should be used instead
     */
    @Deprecated(forRemoval = true)
    public StateBlock(StateBlockSubType subtype, String signature, WorkSolution work, NanoAccount account,
                      String prevHash, NanoAccount repAddr, NanoAmount balance,
                      NanoAccount link) {
        this(subtype, new HexData(signature), work, account, new HexData(prevHash),
                repAddr, balance, parseLinkData(subtype, null, link));
    }
    
    /**
     * Constructs a new state block.
     *
     * @param subtype    the block's subtype
     * @param signature  the block verification signature
     * @param work       the computed work solution
     * @param account    the account's address
     * @param prevHash   the previous block's hash
     * @param repAddr    the representative address of this account
     * @param balance    the balance of the account after this transaction, in raw
     * @param link       the link data for this transaction, encoded as a hexadecimal string
     *
     * @deprecated {@link StateBlockBuilder} builder class should be used instead
     */
    @Deprecated(forRemoval = true)
    public StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
                      HexData prevHash, NanoAccount repAddr, NanoAmount balance, HexData link) {
        this(subtype, signature, work, account, prevHash, repAddr,
                balance, parseLinkData(subtype, link, null));
    }
    
    /**
     * Constructs a new state block.
     *
     * @param subtype   the block's subtype
     * @param signature the block verification signature
     * @param work      the computed work solution
     * @param account   the account's address
     * @param prevHash  the previous block's hash
     * @param repAddr   the representative address of this account
     * @param balance   the balance of the account after this transaction, in raw
     * @param link      the link data for this transaction, encoded as an account
     *
     * @deprecated {@link StateBlockBuilder} builder class should be used instead
     */
    @Deprecated(forRemoval = true)
    public StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
                      HexData prevHash, NanoAccount repAddr, NanoAmount balance,
                      NanoAccount link) {
        this(subtype, signature, work, account, prevHash, repAddr,
                balance, parseLinkData(subtype, null, link));
    }
    
    StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
               HexData prevHash, NanoAccount repAddr, NanoAmount balance,
               HexData linkHex, NanoAccount linkAcc) {
        this(subtype, signature, work, account, prevHash, repAddr,
                balance, parseLinkData(subtype, linkHex, linkAcc));
    }
    
    StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
               HexData prevHash, NanoAccount repAddr, NanoAmount balance, LinkData link) {
        super(BlockType.STATE, signature, work);
        
        if (subtype == null)
            throw new IllegalArgumentException("Subtype cannot be null.");
        if (prevHash == null) throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (!JNH.isValidLength(prevHash, NanoConst.LEN_HASH_B))
            throw new IllegalArgumentException("Previous block hash is an invalid length.");
        if (repAddr == null) throw new IllegalArgumentException("Block representative cannot be null.");
        if (balance == null) throw new IllegalArgumentException("Account balance cannot be null.");
        if (account == null) throw new IllegalArgumentException("Block account cannot be null.");
        
        this.subtype = subtype;
        this.account = account;
        this.prevHash = prevHash;
        this.repAddr = repAddr;
        this.balance = balance;
        this.link = link;
    }
    
    
    /**
     * @return the subtype of the block
     */
    public final StateBlockSubType getSubType() {
        return subtype;
    }
    
    @Override
    public final NanoAccount getAccount() {
        return account;
    }
    
    @Override
    public final HexData getPrevHash() {
        return prevHash;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return repAddr;
    }
    
    @Override
    public final NanoAmount getBalance() {
        return balance;
    }
    
    @Override
    public final LinkData getLink() {
        return link;
    }
    
    @Override
    public BlockIntent getIntent() {
        boolean isOpen = subtype == StateBlockSubType.OPEN || JNH.isZero(prevHash, false);
        boolean isEpoch = subtype == StateBlockSubType.EPOCH || (isOpen && balance.equals(NanoAmount.ZERO));
        boolean isSend = subtype == StateBlockSubType.SEND;
        boolean isReceive = subtype == StateBlockSubType.RECEIVE || (isOpen && !isEpoch);
        
        return new BlockIntent(
                BlockIntent.UncertainBool.valueOf(isSend),
                BlockIntent.UncertainBool.valueOf(isReceive),
                (subtype == StateBlockSubType.SEND || subtype == StateBlockSubType.RECEIVE) ?
                        BlockIntent.UncertainBool.UNKNOWN :
                        BlockIntent.UncertainBool.valueOf(subtype == StateBlockSubType.CHANGE || isOpen),
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
                && Objects.equals(getLink(), sb.getLink())
                && Objects.equals(getRepresentative(), sb.getRepresentative())
                && Objects.equals(getPrevHash(), sb.getPrevHash())
                && Objects.equals(getBalance(), sb.getBalance());
    }
    
    
    @Override
    protected byte[][] generateHashables() {
        return new byte[][] {
                HASH_PREAMBLE,
                getAccount().getPublicKeyBytes(),
                getPrevHash().toByteArray(),
                getRepresentative().getPublicKeyBytes(),
                JNH.leftPadByteArray(getBalance().getAsRaw().toByteArray(), 16, false),
                getLink().asByteArray()
        };
    }
    
    @Override
    protected JsonObject buildJsonObject() {
        JsonObject json = super.buildJsonObject();
        // Add link fields
        json.addProperty("link",            getLink().asHex().toString());
        json.addProperty("link_as_account", getLink().asAccount().toAddress());
        return json;
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
        return JNH.tryRethrow(Block.parse(json), b -> (StateBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a state block.", e));
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
        return JNH.tryRethrow(Block.parse(json), b -> (StateBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a state block.", e));
    }
    
    
    static LinkData parseLinkData(StateBlockSubType subtype, HexData hex, NanoAccount account) {
        // Determine intent
        LinkData.Intent intent = LinkData.Intent.UNUSED;
        switch (subtype) {
            case SEND:
                intent = LinkData.Intent.DESTINATION_ACCOUNT; break;
            case RECEIVE:
            case OPEN:
                intent = LinkData.Intent.SOURCE_HASH; break;
            case EPOCH:
                intent = LinkData.Intent.EPOCH_IDENTIFIER; break;
        }
        return new LinkData(intent, hex, account);
    }
    
}
