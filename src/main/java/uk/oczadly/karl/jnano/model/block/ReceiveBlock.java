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
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>This class is used to represent {@link BlockType#RECEIVE receive} blocks, and the associated data they contain. A
 * {@code receive} block represents a financial transaction where funds are accepted from a corresponding {@code send}
 * block into the account.</p>
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
 *         <td>The block type ({@link BlockType#RECEIVE})</td>
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
 *         <td>{@link #getSourceBlockHash() source}</td>
 *         <td>No</td>
 *         <td>The hash of the corresponding {@code send} block which sent the funds.</td>
 *     </tr>
 * </table>
 */
public class ReceiveBlock extends Block implements IBlockPrevious, IBlockSource {
    
    /** A function which converts a {@link JsonObject} into a {@link ReceiveBlock} instance. */
    public static final Function<JsonObject, ReceiveBlock> DESERIALIZER = json -> new ReceiveBlock(
            JNH.getJson(json, "signature", HexData::new),
            JNH.getJson(json, "work",      WorkSolution::new),
            JNH.getJson(json, "previous",  HexData::new),
            JNH.getJson(json, "source",    HexData::new));
    
    private static final BlockIntent INTENT = new BlockIntent(false, true, false, false, false, false);
    
    
    @Expose @SerializedName("previous")
    private final HexData previousBlockHash;
    
    @Expose @SerializedName("source")
    private final HexData sourceBlockHash;
    
    
    /**
     * Constructs a new receive block.
     *
     * @param signature the signature (may be null)
     * @param work      the work solution (may be null)
     * @param previous  the hash of the previous block
     * @param source    the hash of the source block
     */
    public ReceiveBlock(HexData signature, WorkSolution work, HexData previous, HexData source) {
        super(BlockType.RECEIVE, signature, work);
    
        if (previous == null)
            throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (previous.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Previous block hash is an invalid length.");
        if (source == null)
            throw new IllegalArgumentException("Source block hash cannot be null.");
        if (source.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Source block hash is an invalid length.");
        
        this.previousBlockHash = previous;
        this.sourceBlockHash = source;
    }
    
    
    @Override
    public final HexData getPreviousBlockHash() {
        return previousBlockHash;
    }
    
    @Override
    public final HexData getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof ReceiveBlock)) return false;
        ReceiveBlock rb = (ReceiveBlock)block;
        return super.contentEquals(rb)
                && Objects.equals(getPreviousBlockHash(), rb.getPreviousBlockHash())
                && Objects.equals(getSourceBlockHash(), rb.getSourceBlockHash());
    }
    
    @Override
    protected byte[] calculateHash() {
        return hashBlake2b(
                getPreviousBlockHash().toByteArray(),
                getSourceBlockHash().toByteArray());
    }
    
    /**
     * Parses a {@code receive} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ReceiveBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static ReceiveBlock parse(String json) {
        return JNH.tryRethrow(Block.parse(json), b -> (ReceiveBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a receive block.", e));
    }
    
    /**
     * Parses a {@code receive} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ReceiveBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static ReceiveBlock parse(JsonObject json) {
        return JNH.tryRethrow(Block.parse(json), b -> (ReceiveBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not a receive block.", e));
    }
    
}
