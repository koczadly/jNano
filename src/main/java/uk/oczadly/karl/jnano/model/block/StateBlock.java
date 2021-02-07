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
import uk.oczadly.karl.jnano.model.epoch.EpochUpgrade;
import uk.oczadly.karl.jnano.model.epoch.EpochUpgradeRegistry;
import uk.oczadly.karl.jnano.model.epoch.UnrecognizedEpochException;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.NetworkConstants;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>This class is used to represent {@link BlockType#STATE state} blocks, and the associated data they contain. A
 * {@code state} block can represent a multitude of different actions, distinguished by the {@code subtype}
 * attribute.</p>
 *
 * <p>To construct a new state block, use the provided {@link StateBlockBuilder} builder class. The class constructors
 * can be difficult to work with, and may even be subject to change in the future.</p>
 *
 * <p>The following actions can be performed by a state block:</p>
 * <table summary="Description of the various state block subtypes">
 *     <tr>
 *         <th>Subtype</th>
 *         <th>Action</th>
 *         <th>Link purpose</th>
 *         <th>Legacy equivalent</th>
 *     </tr>
 *     <tr>
 *         <td>{@link StateBlockSubType#OPEN Open}</td>
 *         <td>The first block in an account, also accepting funds from another pending {@code send} transaction
 *         into this account.</td>
 *         <td>{@link LinkData.Intent#SOURCE_HASH Source hash}</td>
 *         <td>{@link OpenBlock}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link StateBlockSubType#SEND Send}</td>
 *         <td>A financial transaction which sends an amount of funds to another account. The account's representative
 *         may also be updated within this block.</td>
 *         <td>{@link LinkData.Intent#DESTINATION_ACCOUNT Destination account}</td>
 *         <td>{@link SendBlock}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link StateBlockSubType#RECEIVE Receive}</td>
 *         <td>A financial transaction which accepts funds from a pending {@code send} block into this account. The
 *         account's representative may also be updated within this block.</td>
 *         <td>{@link LinkData.Intent#SOURCE_HASH Source hash}</td>
 *         <td>{@link ReceiveBlock}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link StateBlockSubType#CHANGE Change}</td>
 *         <td>A special action used to change the chosen representative of the account. While {@code send} and
 *         {@code receive} subtypes can also change the representative, a {@code change} action has no other
 *         purpose.</td>
 *         <td>{@link LinkData.Intent#UNUSED Not used}</td>
 *         <td>{@link ChangeBlock}</td>
 *     </tr>
 *     <tr>
 *         <td>{@link StateBlockSubType#EPOCH Epoch}</td>
 *         <td>A special action used to upgrade the version of the account.</td>
 *         <td>{@link LinkData.Intent#EPOCH_IDENTIFIER Epoch reference}</td>
 *         <td>N/A</td>
 *     </tr>
 * </table>
 *
 * <p>The hash of a block can be calculated and retrieved by calling the {@link #getHash()} method. A block may be
 * signed with a private key using the {@link #sign(HexData)} method, and verified using
 * {@link #verifySignature(NanoAccount)}.</p>
 *
 * <p>The block contains the following fields (note that <em>mutable</em> fields may also hold null values):</p>
 * <table summary="Descriptions of block fields">
 *     <tr>
 *         <th>Attribute</th>
 *         <th>Mutable</th>
 *         <th>Description</th>
 *     </tr>
 *     <tr>
 *         <td>{@link #getType() type}</td>
 *         <td>No</td>
 *         <td>The block type ({@link BlockType#STATE})</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getSignature() signature}</td>
 *         <td>{@link #setSignature(HexData) Yes}</td>
 *         <td>The signature, verifying the account holder created this block.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getWorkSolution() work}</td>
 *         <td>{@link #setWorkSolution(WorkSolution) Yes}</td>
 *         <td>The proof-of-work solution.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getSubType() subtype}</td>
 *         <td>No</td>
 *         <td>The sub-type of the state block.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getAccount() account}</td>
 *         <td>No</td>
 *         <td>The account which owns this block.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getPreviousBlockHash() previous}</td>
 *         <td>No</td>
 *         <td>The hash of the previous block in the account, or all zeroes for {@link StateBlockSubType#OPEN open}
 *         subtypes.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getRepresentative() representative}</td>
 *         <td>No</td>
 *         <td>The address of the representative this account is delegating to.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getBalance() balance}</td>
 *         <td>No</td>
 *         <td>The balance of the account <em>after</em> this block has been processed.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getLink() link}</td>
 *         <td>No</td>
 *         <td>A miscellaneous 32-byte link data associated with this transaction.</td>
 *     </tr>
 * </table>
 */
public final class StateBlock extends Block implements IBlockLink, IBlockBalance, IBlockPrevious, IBlockRepresentative,
        IBlockAccount {
    
    /** A function which converts a {@link JsonObject} into a {@link StateBlock} instance. */
    public static final Function<JsonObject, StateBlock> DESERIALIZER = json -> new StateBlock(
            JNH.getJson(json, "subtype",         StateBlockSubType::getFromName),
            JNH.getJson(json, "signature",       HexData::new),
            JNH.getJson(json, "work",            WorkSolution::new),
            JNH.getJson(json, "account",         NanoAccount::parseAddress),
            JNH.getJson(json, "previous",        HexData::new),
            JNH.getJson(json, "representative",  NanoAccount::parseAddress),
            JNH.getJson(json, "balance",         NanoAmount::valueOfRaw),
            JNH.getJson(json, "link",            HexData::new),
            JNH.getJson(json, "link_as_account", NanoAccount::parseAddress));
    
    /** Prefix for block hashing. */
    private static final byte[] HASH_PREAMBLE = JNH.leftPadByteArray(new byte[] {6}, 32, false);
    
    
    @Expose @SerializedName("account")
    private final NanoAccount account;
    
    @Expose @SerializedName("previous")
    private final HexData previous;
    
    @Expose @SerializedName("representative")
    private final NanoAccount rep;
    
    @Expose @SerializedName("balance")
    private final NanoAmount balance;
    
    @Expose @SerializedName("subtype")
    private final StateBlockSubType subtype;
    
    private final transient LinkData link;
    
    
    /**
     * Constructs a new state block.
     *
     * @param subtype        the subtype action
     * @param signature      the signature (may be null)
     * @param work           the work solution (may be null)
     * @param account        the account which owns this block
     * @param previous       the hash of the previous block
     * @param representative the representative for this account
     * @param balance        the balance of this account
     * @param link           the miscellaneous link data of this transaction
     *
     * @see StateBlockBuilder
     */
    public StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
               HexData previous, NanoAccount representative, NanoAmount balance, HexData link) {
        this(subtype, signature, work, account, previous, representative, balance, link, null);
    }
    
    StateBlock(StateBlockSubType subtype, HexData signature, WorkSolution work, NanoAccount account,
               HexData previous, NanoAccount rep, NanoAmount balance, HexData linkHex, NanoAccount linkAcc) {
        super(BlockType.STATE, signature, work);
        
        if (subtype == null)
            throw new IllegalArgumentException("Subtype cannot be null.");
        if (account == null)
            throw new IllegalArgumentException("Account cannot be null.");
        if (previous == null)
            throw new IllegalArgumentException("Previous hash cannot be null.");
        if (previous.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Previous hash length is incorrect.");
        if (rep == null)
            throw new IllegalArgumentException("Representative cannot be null.");
        if (balance == null)
            throw new IllegalArgumentException("Account balance cannot be null.");
        
        this.subtype = subtype;
        this.account = account;
        this.previous = previous;
        this.rep = rep;
        this.balance = balance;
        this.link = parseLinkData(subtype, linkHex, linkAcc);
    }
    
    
    /**
     * Returns the block's sub-type.
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
    public final HexData getPreviousBlockHash() {
        return previous;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return rep;
    }
    
    @Override
    public final NanoAmount getBalance() {
        return balance;
    }
    
    @Override
    public final LinkData getLink() {
        return link;
    }
    
    /**
     * Tests whether the signature is valid and was signed by the correct account, also validating epoch blocks for
     * the live Nano network.
     *
     * <p>For standard transactions, this uses the {@link #getAccount() block's account}. For externally-created
     * epoch blocks, the signature is checked using the {@link EpochUpgrade#getSigner() expected signer account}
     * rather than the block's holding account.</p>
     *
     * <p><b>NOTE:</b> This method is only valid for epoch blocks on the {@link NetworkConstants#NANO live Nano
     * network}. Other networks, such as the beta network or Banano use different signing accounts for epoch upgrades.
     * The jNano library must also be updated to the latest version to ensure that the epoch identifiers can be
     * recognized. For non-epoch subtypes, this method will function as expected with all networks.</p>
     *
     * @return true if the signature is correct, false if not <em>or</em> if the {@code signature} is currently null
     * @throws UnrecognizedEpochException if the block is an epoch block, and the epoch version was not recognized
     *
     * @see #verifySignature(EpochUpgradeRegistry)
     */
    public boolean verifySignature() {
        return verifySignature(NetworkConstants.NANO.getEpochUpgrades());
    }
    
    
    /**
     * Tests whether the signature is valid and was signed by the correct account, also validating epoch blocks.
     *
     * <p>For standard transactions, this uses the {@link #getAccount() block's account}. For externally-created
     * epoch blocks, the signature is checked using the {@link EpochUpgrade#getSigner() expected signer account}
     * rather than the block's holding account.</p>
     *
     * @param epochRegistry the epoch registry to be used when checking epoch blocks
     * @return true if the signature is correct, false if not <em>or</em> if the {@code signature} is currently null
     * @throws UnrecognizedEpochException if the block is an epoch block, and the epoch version was not recognized
     *
     * @see #verifySignature()
     * @see NetworkConstants
     */
    public boolean verifySignature(EpochUpgradeRegistry epochRegistry) {
        if (epochRegistry == null) throw new IllegalArgumentException("Epoch registry cannot be null.");
        if (getSignature() == null) return false;
        
        if (getSubType() == StateBlockSubType.EPOCH) {
            EpochUpgrade epoch = epochRegistry.ofIdentifier(getLink().asHex());
            return verifySignature(epoch.getSigner().orElse(getAccount()));
        } else {
            return verifySignature(getAccount());
        }
    }
    
    @Override
    public BlockIntent getIntent() {
        boolean isOpen = subtype == StateBlockSubType.OPEN || JNH.isZero(previous, false);
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
                && Objects.equals(getPreviousBlockHash(), sb.getPreviousBlockHash())
                && Objects.equals(getBalance(), sb.getBalance());
    }
    
    @Override
    protected byte[][] hashables() {
        return new byte[][] {
                HASH_PREAMBLE,
                getAccount().getPublicKeyBytes(),
                getPreviousBlockHash().toByteArray(),
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
