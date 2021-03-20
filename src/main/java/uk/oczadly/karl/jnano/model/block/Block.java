/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.CryptoUtil;

import java.util.Objects;

/**
 * An abstract class which represents a Nano block.
 *
 * <p>Blocks are semi-immutable; optional fields, such as the signature and work values, may be updated and changed
 * through the use of setters. All other fields, however, should be immutable after construction.</p>
 *
 * <p>For a concrete version of this class, use {@link StateBlock} (or {@link SendBlock}, {@link ReceiveBlock},
 * {@link ChangeBlock} and {@link OpenBlock} for legacy types).</p>
 *
 * <p>To convert a JSON string/object to a Block instance, use the {@link #parse(JsonObject)} static method or the
 * {@link BlockDeserializer} class. This class can automatically be serialized and deserialized by Gson, as a
 * built-in JSON adapter is provided and automatically registered.</p>
 */
@JsonAdapter(BlockDeserializer.JsonAdapter.class)
public abstract class Block implements IBlock {
    
    private transient volatile HexData hash;
    
    @Expose @SerializedName("type")
    private final String type;
    private transient final BlockType typeEnum;
    
    @Expose @SerializedName("signature")
    private volatile HexData signature;
    
    @Expose @SerializedName("work")
    private volatile WorkSolution work;
    
    
    private Block() {
        throw new AssertionError("A block type must be specified.");
    }
    
    /**
     * @param type          the block type, as a string
     * @param signature     the block signature, or null
     * @param work  the work solution, or null
     */
    protected Block(String type, HexData signature, WorkSolution work) {
        this(null, type, signature, work);
    }
    
    /**
     * @param type          the block type
     * @param signature     the block signature, or null
     * @param work  the work solution, or null
     * @see #Block(String, HexData, WorkSolution)
     */
    protected Block(BlockType type, HexData signature, WorkSolution work) {
        this(type, type.getProtocolName(), signature, work);
    }
    
    private Block(BlockType type, String typeStr, HexData signature, WorkSolution work) {
        if (type == null && typeStr == null)
            throw new IllegalArgumentException("Block type cannot be null.");
        this.type = typeStr.toLowerCase();
        this.typeEnum = type;
        this.signature = signature;
        this.work = work;
    }
    
    
    @Override
    public final HexData getHash() {
        if (hash == null) {
            synchronized (this) {
                if (hash == null)
                    hash = CryptoUtil.hash(hashables());
            }
        }
        return hash;
    }
    
    /**
     * @return a 32-length array of bytes, representing the hash of this block
     * @deprecated Use of {@link HexData#toByteArray()} on {@link #getHash()} is preferred.
     */
    @Deprecated
    public final byte[] getHashBytes() {
        return getHash().toByteArray();
    }
    
    @Override
    public final BlockType getType() {
        return typeEnum;
    }
    
    @Override
    public final String getTypeString() {
        return type;
    }
    
    @Override
    public final synchronized HexData getSignature() {
        return signature;
    }
    
    /**
     * Sets or updates the signature for this block.
     * @param signature the new signature, or null (will render the block incomplete)
     */
    public synchronized void setSignature(HexData signature) {
        if (signature != null && signature.length() != NanoConst.LEN_SIGNATURE_B)
            throw new IllegalArgumentException("Block signature is an invalid length.");
        this.signature = signature;
    }
    
    /**
     * Signs this block using the provided private key, and updates the signature attribute of this block.
     *
     * <p>The computed value will overwrite the existing {@code signature} value in this block. Note that this will
     * not update any {@code account} fields the block may have, even if the supplied private key does not match the
     * account.</p>
     *
     * @param privateKey the private key of the signer (32-byte value)
     * @return the computed signature value
     */
    public final synchronized HexData sign(HexData privateKey) {
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (privateKey.length() != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Private key length is invalid.");
    
        HexData sig = CryptoUtil.sign(sigBytes(), privateKey.toByteArray());
        setSignature(sig);
        return sig;
    }
    
    /**
     * Tests whether the signature is valid and was signed by the specified account.
     *
     * <p>Be aware that some special blocks (such as epoch upgrades) may be signed by an account other than the
     * block's owner. These blocks have their own specific signers and behaviour, and should provide their own
     * method in addition to this for checking the validity of the signature.</p>
     *
     * @param account the signer account (public key) to test
     * @return true if the specified account is the signer of this block's signature, or false if not <em>or</em> if
     *         the {@code signature} field is null
     */
    public final synchronized boolean verifySignature(NanoAccount account) {
        if (account == null)
            throw new IllegalArgumentException("Account cannot be null.");
        if (signature == null) return false;
        return CryptoUtil.verifySig(sigBytes(), signature.toByteArray(), account.getPublicKeyBytes());
    }
    
    /**
     * Returns an array of byte arrays, used for signature creation and validation.
     * @return the content bytes
     */
    protected byte[][] sigBytes() {
        return new byte[][] { getHash().toByteArray() };
    }
    
    /**
     * Returns an array of byte arrays to be hashed in the given order.
     * @return the hashables
     */
    protected abstract byte[][] hashables();
    
    @Override
    public final WorkSolution getWorkSolution() {
        return work;
    }
    
    /**
     * Sets or updates the work solution for this block.
     * @param work the new work value, or null (will render the block incomplete)
     */
    public void setWorkSolution(WorkSolution work) {
        this.work = work;
    }
    
    @Override
    public boolean isComplete() {
        return getWorkSolution() != null && getSignature() != null;
    }
    
    /**
     * Returns the block as a JSON string, filling blank fields.
     * <p>Equivalent to calling {@code toJsonString(true)}.</p>
     * @return a JSON representation of this block
     */
    public final String toJsonString() {
        return toJsonString(true);
    }
    
    /**
     * Returns the block as a JSON string.
     * @param fillBlanks if true, null properties will be filled with dummy data
     * @return a JSON representation of this block
     */
    public final String toJsonString(boolean fillBlanks) {
        return toJsonObject(fillBlanks).toString();
    }
    
    /**
     * Returns the block as a JSON object, filling blank fields.
     * <p>Equivalent to calling {@code toJsonObject(true)}.</p>
     * @return a JSON representation of this block, as a Gson {@link JsonObject}
     */
    public final JsonObject toJsonObject() {
        return toJsonObject(true);
    }
    
    /**
     * Returns the block as a JSON object.
     * @param fillBlanks if true, null properties will be filled with dummy data
     * @return a JSON representation of this block, as a Gson {@link JsonObject}
     */
    public final JsonObject toJsonObject(boolean fillBlanks) {
        JsonObject json = buildJsonObject();
        if (fillBlanks) {
            if (signature == null)
                json.addProperty("signature", JNC.ZEROES_128);
            if (work == null)
                json.addProperty("work", JNC.ZEROES_16);
            fillJsonBlanks(json);
        }
        return json;
    }
    
    /**
     * Fill blank or missing parameters with {@link JsonObject#addProperty(String, String)}.
     * <p>This method should be overridden by subclasses where applicable.</p>
     * @param json the JSON object to fill
     */
    protected void fillJsonBlanks(JsonObject json) {}
    
    /**
     * Build a JsonObject that represents this instance.
     * @return the JsonObject representing this block
     */
    protected JsonObject buildJsonObject() {
        return JNC.GSON.toJsonTree(this).getAsJsonObject();
    }
    
    /**
     * Returns a prettified JSON representation of this block.
     * @return this block as a JSON string
     */
    @Override
    public final String toString() {
        return JNC.GSON_PRETTY.toJson(toJsonObject(false)); // Pretty print
    }
    
    /**
     * Compares this block with the given object, and tests for equality with the type and hash.
     *
     * <p>Note that this method will only compare the types and hash fields of the block, and will not compare
     * unhashed values, including the {@code work} and {@code signature} fields. For these, you should use the
     * {@link #contentEquals(Block)} method instead.</p>
     *
     * @param obj the object to compare against
     * @return {@code true} if the objects are equal
     * @see #contentEquals(Block)
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Block)) return false; // Not a Block
        Block block = (Block)obj;
        if (!getTypeString().equalsIgnoreCase(block.getTypeString()))
            return false; // Type doesn't match
        return getHash().equals(block.getHash());
    }
    
    /**
     * Compares this block with the given block, and tests for equality.
     *
     * <p>Unlike the {@link #equals(Object)} method, this method will additionally compare the unhashed fields which
     * can vary without affecting the hash of the block.</p>
     *
     * @param block the block to compare against
     * @return {@code true} if the blocks are equal in content
     */
    public boolean contentEquals(Block block) {
        return equals(block)
                && Objects.equals(getSignature(), block.getSignature())
                && Objects.equals(getWorkSolution(), block.getWorkSolution());
    }
    
    @Override
    public final int hashCode() {
        return getHash().hashCode();
    }
    
    /**
     * Returns a block which is an identical clone of this block.
     * @return a clone of this block
     */
    public abstract Block clone();
    
    
    /**
     * Parses a block from a given JSON string using the default deserializer.
     * <p>The following types are supported: {@link StateBlock state}, {@link ChangeBlock change}, {@link OpenBlock
     * open}, {@link ReceiveBlock receive}, {@link SendBlock send}.</p>
     * @param json the JSON data to parse from
     * @return a new {@link Block} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     */
    public static Block parse(String json) {
        try {
            return parse(JNH.parseJson(json));
        } catch (BlockDeserializer.BlockParseException e) {
            throw e;
        } catch (JsonParseException e) {
            throw new BlockDeserializer.BlockParseException(e);
        }
    }
    
    /**
     * Parses a block from a given {@link JsonObject} instance using the default deserializer.
     * <p>The following types are supported: {@link StateBlock state}, {@link ChangeBlock change}, {@link OpenBlock
     * open}, {@link ReceiveBlock receive}, {@link SendBlock send}.</p>
     * @param json the JSON data to parse from
     * @return a new {@link Block} constructed from the given JSON data
     * @throws BlockDeserializer.BlockParseException if the block cannot be correctly parsed
     * @see BlockDeserializer
     */
    public static Block parse(JsonObject json) {
        return JNC.BLOCK_DESERIALIZER.deserialize(json);
    }
    
}
