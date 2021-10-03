/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>This class is used to represent {@link BlockType#CHANGE change} blocks, and the associated data they contain. A
 * {@code change} block represents a special action, where the chosen representative of an account is changed.</p>
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
 *         <td>The block type ({@link BlockType#CHANGE})</td>
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
 *         <td>{@link #getRepresentative() representative}</td>
 *         <td>No</td>
 *         <td>The address of the representative this account is delegating to.</td>
 *     </tr>
 * </table>
 */
public class ChangeBlock extends Block implements IBlockPrevious, IBlockRepresentative {
    
    /** A function which converts a {@link JsonObject} into a {@link ChangeBlock} instance. */
    public static final Function<JsonObject, ChangeBlock> DESERIALIZER = json -> new ChangeBlock(
            JNH.getJson(json, "signature",      HexData::new),
            JNH.getJson(json, "work",           WorkSolution::new),
            JNH.getJson(json, "previous",       HexData::new),
            JNH.getJson(json, "representative", NanoAccount::parseAddress));
    
    private static final BlockIntent INTENT = new BlockIntent(false, false, true, false, false, false);
    
    
    @Expose private final HexData previous;
    @Expose private final NanoAccount representative;
    
    
    /**
     * Constructs a new change block.
     *
     * @param signature      the signature (may be null)
     * @param work           the work solution (may be null)
     * @param previous       the hash of the previous block
     * @param representative the representative for this account
     */
    public ChangeBlock(HexData signature, WorkSolution work, HexData previous, NanoAccount representative) {
        super(BlockType.CHANGE, signature, work);
    
        if (previous == null)
            throw new IllegalArgumentException("Previous block hash cannot be null.");
        if (previous.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Previous block hash is an invalid length.");
        if (representative == null)
            throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.previous = previous;
        this.representative = representative;
    }
    
    
    @Override
    public final HexData getPreviousBlockHash() {
        return previous;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representative;
    }
    
    @Override
    public BlockIntent getIntent() {
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof ChangeBlock)) return false;
        ChangeBlock cb = (ChangeBlock)block;
        return super.contentEquals(cb)
                && Objects.equals(getPreviousBlockHash(), cb.getPreviousBlockHash())
                && Objects.equals(getRepresentative(), cb.getRepresentative());
    }
    
    @Override
    protected byte[][] hashables() {
        return new byte[][] {
                getPreviousBlockHash().toByteArray(),
                getRepresentative().getPublicKeyBytes()
        };
    }
    
    @Override
    public ChangeBlock clone() {
        return new ChangeBlock(getSignature(), getWorkSolution(), previous, representative);
    }
    
    
    /**
     * Parses a {@code change} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ChangeBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static ChangeBlock parse(String json) {
        return parse(json, ChangeBlock.class);
    }
    
    /**
     * Parses a {@code change} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link ChangeBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static ChangeBlock parse(JsonObject json) {
        return parse(json, ChangeBlock.class);
    }
   
}
