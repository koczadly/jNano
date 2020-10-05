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
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Objects;

/**
 * An abstract class which represents a Nano block.
 * <p>For a concrete version of this class, use {@link StateBlock} (or {@link SendBlock}, {@link ReceiveBlock},
 * {@link ChangeBlock} and {@link OpenBlock} for legacy types).</p>
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
    private final HexData signature;
    
    @Expose @SerializedName("work")
    private final WorkSolution workSolution;
    
    
    private Block() {
        throw new AssertionError("A block type must be specified.");
    }
    
    /**
     * @param type          the block type, as a string
     * @param signature     the block signature, or null
     * @param workSolution  the work solution, or null
     */
    protected Block(String type, HexData signature, WorkSolution workSolution) {
        this(null, type, signature, workSolution);
    }
    
    /**
     * @param type          the block type
     * @param signature     the block signature, or null
     * @param workSolution  the work solution, or null
     * @see #Block(String, HexData, WorkSolution)
     */
    protected Block(BlockType type, HexData signature, WorkSolution workSolution) {
        this(type, type.getProtocolName(), signature, workSolution);
    }
    
    private Block(BlockType type, String typeStr, HexData signature, WorkSolution workSolution) {
        if (type == null && typeStr == null)
            throw new IllegalArgumentException("Block type cannot be null.");
        if (!JNH.isValidLength(signature, NanoConst.LEN_SIGNATURE_B))
            throw new IllegalArgumentException("Block signature is an invalid length.");
        
        this.type = typeStr.toLowerCase();
        this.typeEnum = type;
        this.signature = signature;
        this.workSolution = workSolution;
    }
    
    
    @Override
    public final HexData getHash() {
        if (hash == null) {
            synchronized (this) {
                if (hash == null) {
                    hash = new HexData(calculateHashBytes(), NanoConst.LEN_HASH_B);
                }
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
    public final HexData getSignature() {
        return signature;
    }
    
    @Override
    public final WorkSolution getWorkSolution() {
        return workSolution;
    }
    
    @Override
    public boolean isComplete() {
        return getWorkSolution() != null && getSignature() != null;
    }
    
    /**
     * @return an array of hashable byte arrays in the correct sequence, or null if not supported
     */
    protected abstract byte[][] generateHashables();
    
    /**
     * Calculates the hash of this block as a sequence of bytes.
     * This method will not use the built-in cache, and will generate the hash from the sequence of hashables.
     * @return the generated block hash, as a byte array
     */
    protected final byte[] calculateHashBytes() {
        byte[][] hashables = generateHashables();
        if (hashables == null) return null;
        return JNH.blake2b(32, hashables);
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
                json.addProperty("signature", JNH.ZEROES_128);
            if (workSolution == null)
                json.addProperty("work", JNH.ZEROES_16);
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
        return JNH.GSON.toJsonTree(this).getAsJsonObject();
    }
    
    @Override
    public final String toString() {
        return this.toJsonString();
    }
    
    /**
     * Compares this block with the given object, and tests for equality with the type and hash.
     *
     * <p>Note that this method will only compare the types and hash fields of the block, and will not compare
     * unhashed values, including the {@code work} and {@code signature} fields.</p>
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
        if (!getTypeString().equalsIgnoreCase(block.getTypeString())) return false; // Type doesnt match
        return getHash() != null && block.getHash() != null && Objects.equals(getHash(), block.getHash());
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
        return BlockDeserializer.DEFAULT.deserialize(json);
    }
    
}
