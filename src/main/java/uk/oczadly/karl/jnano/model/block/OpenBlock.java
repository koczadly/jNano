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
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockRepresentative;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockSource;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>This class is used to represent {@link BlockType#OPEN open} blocks, and the associated data they contain. An
 * {@code open} block represents a financial transaction where funds are accepted from a corresponding {@code send}
 * block into the account. In addition to this, an open block is the first block in an account, effectively
 * "opening" the account.</p>
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
 *         <td>The block type ({@link BlockType#OPEN})</td>
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
 *         <td>{@link #getSourceBlockHash() source}</td>
 *         <td>No</td>
 *         <td>The hash of the corresponding {@code send} block which sent the funds.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getAccount() account}</td>
 *         <td>No</td>
 *         <td>The account being opened, which created this block.</td>
 *     </tr>
 *     <tr>
 *         <td>{@link #getRepresentative() representative}</td>
 *         <td>No</td>
 *         <td>The address of the representative this account is delegating to.</td>
 *     </tr>
 * </table>
 */
public class OpenBlock extends Block implements IBlockSource, IBlockAccount, IBlockRepresentative {
    
    /** A function which converts a {@link JsonObject} into a {@link OpenBlock} instance. */
    public static final Function<JsonObject, OpenBlock> DESERIALIZER = json -> new OpenBlock(
            JNH.getJson(json, "signature",      HexData::new),
            JNH.getJson(json, "work",           WorkSolution::new),
            JNH.getJson(json, "source",         HexData::new),
            JNH.getJson(json, "account",        NanoAccount::parseAddress),
            JNH.getJson(json, "representative", NanoAccount::parseAddress));
    
    private static final BlockIntent INTENT = new BlockIntent(false, true, true, true, false, false);
    private static final BlockIntent INTENT_GENESIS = new BlockIntent(false, true, true, true, false, true);
    
    
    @Expose @SerializedName("source")
    private final HexData sourceBlockHash;
    
    @Expose @SerializedName("account")
    private final NanoAccount accountAddress;
    
    @Expose @SerializedName("representative")
    private final NanoAccount representativeAccount;
    
    
    /**
     * Constructs a new open block.
     *
     * @param signature      the signature (may be null)
     * @param work           the work solution (may be null)
     * @param source         the hash of the source block
     * @param account        the account which owns this block
     * @param representative the representative for this account
     */
    public OpenBlock(HexData signature, WorkSolution work, HexData source, NanoAccount account,
                     NanoAccount representative) {
        super(BlockType.OPEN, signature, work);
    
        if (source == null)
            throw new IllegalArgumentException("Source block hash cannot be null.");
        if (source.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Source block hash is an invalid length.");
        if (account == null)
            throw new IllegalArgumentException("Block account cannot be null.");
        if (representative == null)
            throw new IllegalArgumentException("Block representative cannot be null.");
        
        this.sourceBlockHash = source;
        this.accountAddress = account;
        this.representativeAccount = representative;
    }
    
    
    @Override
    public final HexData getSourceBlockHash() {
        return sourceBlockHash;
    }
    
    @Override
    public final NanoAccount getAccount() {
        return accountAddress;
    }
    
    @Override
    public final NanoAccount getRepresentative() {
        return representativeAccount;
    }
    
    /**
     * Tests whether the signature is valid and was signed by the correct account.
     *
     * @return true if the signature is correct, false if not <em>or</em> if the {@code signature} is currently null
     */
    public boolean verifySignature() {
        return verifySignature(getAccount());
    }
    
    @Override
    public BlockIntent getIntent() {
        if (getSourceBlockHash().toHexString().equals(getAccount().toPublicKey())
                && getAccount().equals(getRepresentative())) {
            return INTENT_GENESIS; // Genesis block special case
        }
        return INTENT;
    }
    
    @Override
    public boolean contentEquals(Block block) {
        if (!(block instanceof OpenBlock)) return false;
        OpenBlock ob = (OpenBlock)block;
        return super.contentEquals(ob)
                && Objects.equals(getAccount(), ob.getAccount())
                && Objects.equals(getRepresentative(), ob.getRepresentative())
                && Objects.equals(getSourceBlockHash(), ob.getSourceBlockHash());
    }
    
    @Override
    protected byte[] calculateHash() {
        return hashBlake2b(
                getSourceBlockHash().toByteArray(),
                getRepresentative().getPublicKeyBytes(),
                getAccount().getPublicKeyBytes());
    }
    
    
    /**
     * Parses an {@code open} block from a given JSON string using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link OpenBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(String)
     */
    public static OpenBlock parse(String json) {
        return JNH.tryRethrow(Block.parse(json), b -> (OpenBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not an open block.", e));
    }
    
    /**
     * Parses an {@code open} block from a given {@link JsonObject} instance using the default deserializer.
     * @param json the JSON data to parse from
     * @return a new {@link OpenBlock} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     * @see Block#parse(JsonObject)
     */
    public static OpenBlock parse(JsonObject json) {
        return JNH.tryRethrow(Block.parse(json), b -> (OpenBlock)b,
                e -> new BlockDeserializer.BlockParseException("Block is not an open block.", e));
    }
    
}
