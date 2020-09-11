/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Arrays;

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
    
    /**
     * The length of a hash string in hexadecimal. Multiply by 4 for number of bits.
     */
    protected static final int HASH_LENGTH = 64;
    
    
    @Expose @SerializedName("hash")
    private volatile String hash;
    
    private transient volatile byte[] hashBytes;
    
    @Expose @SerializedName("type")
    private final String type;
    private transient final BlockType typeEnum;
    
    @Expose @SerializedName("signature")
    private final String signature;
    
    @Expose @SerializedName("work")
    private final WorkSolution workSolution;
    
    
    private Block() {
        throw new AssertionError("A block type must be specified.");
    }
    
    /**
     * @param type the block type, as a string
     */
    protected Block(String type) {
        this(type, null, null, null);
    }
    
    /**
     * @param type the block type
     */
    protected Block(BlockType type) {
        this(type, null, null, null);
    }
    
    /**
     * @param type          the block type, as a string
     * @param hash          the block hash, or null (for performance improvements only)
     * @param signature     the block signature
     * @param workSolution  the work solution
     */
    protected Block(String type, String hash, String signature, WorkSolution workSolution) {
        this(null, type, hash, signature, workSolution);
    }
    
    /**
     * @param type          the block type
     * @param hash          the block hash, or null (for performance improvements only)
     * @param signature     the block signature
     * @param workSolution  the work solution
     */
    protected Block(BlockType type, String hash, String signature, WorkSolution workSolution) {
        this(type, type.getProtocolName(), hash, signature, workSolution);
    }
    
    private Block(BlockType type, String typeStr, String hash, String signature, WorkSolution workSolution) {
        if (type == null && typeStr == null)
            throw new IllegalArgumentException("Block type cannot be null.");
        if (!JNH.isValidHex(hash, HASH_LENGTH))
            throw new IllegalArgumentException("Block hash is invalid.");
        if (!JNH.isValidHex(signature, 128))
            throw new IllegalArgumentException("Block signature is invalid.");
        
        this.type = typeStr.toLowerCase();
        this.typeEnum = type;
        this.hash = hash != null ? hash.toUpperCase() : null;
        this.signature = signature != null ? signature.toUpperCase() : null;
        this.workSolution = workSolution;
    }
    
    
    /**
     * {@inheritDoc}
     * Returns the block hash, or attempts to calculate it if the hash value was not present.
     */
    @Override
    public final String getHash() {
        if (hash == null) {
            synchronized (this) {
                if (hash == null) {
                    hash = JNH.ENC_16.encode(generateHashBytes());
                }
            }
        }
        return hash;
    }
    
    /**
     * @return a 32-length array of bytes, representing the hash of this block
     */
    public final byte[] getHashBytes() {
        generateHashBytes();
        return Arrays.copyOf(hashBytes, hashBytes.length);
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
    public final String getSignature() {
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
    
    protected final byte[] calculateHashBytes() {
        byte[][] hashables = generateHashables();
        if (hashables == null) return null;
        return JNH.blake2b(32, hashables);
    }
    
    private byte[] generateHashBytes() {
        if (hashBytes == null) {
            synchronized (this) {
                if (hashBytes == null) {
                    if (hash != null) {
                        // Decode from existing hash string
                        hashBytes = JNH.ENC_16.decode(hash);
                    } else {
                        hashBytes = calculateHashBytes();
                    }
                }
            }
        }
        return hashBytes;
    }
    
    
    /**
     * @return a JSON representation of this block
     */
    public final String toJsonString() {
        return toJsonString(true);
    }
    
    /**
     * @param fillBlanks if true, null properties will be filled with dummy data
     * @return a JSON representation of this block
     */
    public final String toJsonString(boolean fillBlanks) {
        return getJsonObject(fillBlanks).toString();
    }
    
    /**
     * @return a JSON representation of this block, as a Gson {@link JsonObject}
     */
    public final JsonObject getJsonObject() {
        return getJsonObject(true);
    }
    
    /**
     * @param fillBlanks if true, null properties will be filled with dummy data
     * @return a JSON representation of this block, as a Gson {@link JsonObject}
     */
    public final JsonObject getJsonObject(boolean fillBlanks) {
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
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block)o;
        return Arrays.equals(generateHashBytes(), block.generateHashBytes());
    }
    
    @Override
    public final int hashCode() {
        return getHash() != null ? getHash().hashCode() : 0;
    }
    
    /**
     * Parses a block from a given JSON string using the default deserializer.
     * <p>The following types are supported: {@link StateBlock state}, {@link ChangeBlock change}, {@link OpenBlock
     * open}, {@link ReceiveBlock receive}, {@link SendBlock send}.</p>
     * @param json the json to parse from
     * @return a block object derived from the provided JSON
     * @see BlockDeserializer
     */
    public static Block parse(String json) {
        return parse(JsonParser.parseString(json).getAsJsonObject());
    }
    
    /**
     * Parses a block from a given {@link JsonObject} instance using the default deserializer.
     * <p>The following types are supported: {@link StateBlock state}, {@link ChangeBlock change}, {@link OpenBlock
     * open}, {@link ReceiveBlock receive}, {@link SendBlock send}.</p>
     * @param json the json to parse from
     * @return a block object derived from the provided JSON
     * @see BlockDeserializer
     */
    public static Block parse(JsonObject json) {
        return BlockDeserializer.DEFAULT.deserialize(json);
    }
    
}
