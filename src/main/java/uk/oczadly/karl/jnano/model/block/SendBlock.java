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
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockBalance;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>This class is used to represent {@link BlockType#SEND send} blocks, and the associated data they contain. A
 * {@code send} block represents a financial transaction where funds are sent from the publishing account to
 * another.</p>
 *
 * <p><b>Note that this is a legacy block and has since been officially deprecated.</b> This class is only used to
 * represent existing legacy blocks of this type, and all newly-issued blocks <em>must</em> be
 * {@link StateBlock state} blocks.</p>
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
 *         <td>The block type ({@link BlockType#SEND})</td>
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
 *         <td>{@link #getPreviousBlockHash() previous}</td>
 *         <td>No</td>
 *         <td>The hash of the previous block in the account.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getDestinationAccount() destination}</td>
 *         <td>No</td>
 *         <td>The destination account which the funds are being sent to.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getBalance() balance}</td>
 *         <td>No</td>
 *         <td>The balance of the account <em>after</em> this block has been processed.</td>
 *     </tr>
 * </table>
 */
public class SendBlock extends Block implements IBlockPrevious, IBlockBalance {
    
    /** A function which converts a {@link JsonObject} into a {@link SendBlock} instance. */
    public static final Function<JsonObject, SendBlock> DESERIALIZER = json -> new SendBlock(
            JNH.getJson(json, "signature",   HexData::new),
            JNH.getJson(json, "work",        WorkSolution::new),
            JNH.getJson(json, "previous",    HexData::new),
            JNH.getJson(json, "destination", NanoAccount::parseAddress),
            JNH.getJson(json, "balance",     NanoAmount::valueOfRaw));
    
    private static final BlockIntent INTENT = new BlockIntent(true, false, false, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private final HexData previousBlockHash;
    
    @Expose @SerializedName("destination")
    private final NanoAccount destinationAccount;
    
    @Expose @SerializedName("balance")
    private final NanoAmount balance;
    
    
    /**
     * Constructs a new send block.
     *
     * @param signature   the signature (may be null)
     * @param work        the work solution (may be null)
     * @param previous    the hash of the previous block
     * @param destination the destination account
     * @param balance     the balance of this account
     */
    public SendBlock(HexData signature, WorkSolution work, HexData previous, NanoAccount destination,
                     NanoAmount balance) {
        super(BlockType.SEND, signature, work);
    
        if (previous == null)
            throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (previous.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Previous block hash is an invalid length.");
        if (destination == null)
            throw new IllegalArgumentException("Block destination account cannot be null.");
        if (balance == null)
            throw new IllegalArgumentException("Account balance cannot be null.");
        
        this.previousBlockHash = previous;
        this.destinationAccount = destination;
        this.balance = balance;
    }
    
    
    @Override
    public final HexData getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    /**
     * @return the destination account which the funds will be sent to
     */
    public final NanoAccount getDestinationAccount() {
        return destinationAccount;
    }
    
    @Override
    public final NanoAmount getBalance() {
        return balance;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof SendBlock)) return false;
        SendBlock sb = (SendBlock)block;
        return super.contentEquals(sb)
                && Objects.equals(getBalance(), sb.getBalance())
                && Objects.equals(getDestinationAccount(), sb.getDestinationAccount())
                && Objects.equals(getPreviousBlockHash(), sb.getPreviousBlockHash());
    }
    
    @Override
    protected byte[] calculateHash() {
        return hashBlake2b(
                getPreviousBlockHash().toByteArray(),
                getDestinationAccount().getPublicKeyBytes(),
                JNH.leftPadByteArray(getBalance().getAsRaw().toByteArray(), 16, false));
    }
    
    
    /**
     * Parses a {@code send} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link SendBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static SendBlock parse(String json) {
        return JNH.tryRethrow(Block.parse(json), b -> (SendBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a send block.", e));
    }
    
    /**
     * Parses a {@code send} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link SendBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static SendBlock parse(JsonObject json) {
        return JNH.tryRethrow(Block.parse(json), b -> (SendBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a send block.", e));
    }
    
}
