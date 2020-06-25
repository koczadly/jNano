package uk.oczadly.karl.jnano.model.block;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.JNanoHelper;
import uk.oczadly.karl.jnano.internal.gsonadapters.BlockAdapter;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlock;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.Arrays;

@JsonAdapter(BlockAdapter.class)
public abstract class Block implements IBlock {
    
    private static final BlockDeserializer BLOCK_DESERIALIZER = new BlockDeserializer();
    
    
    @Expose @SerializedName("hash")
    private volatile String hash;
    
    private transient volatile byte[] hashBytes;
    
    @Expose @SerializedName("type")
    private final BlockType type;
    
    @Expose @SerializedName("signature")
    private final String signature;
    
    @Expose @SerializedName("work")
    private final WorkSolution workSolution;
    
    private volatile JsonObject jsonRepresentation;
    
    
    protected Block(BlockType type) {
        this(type, null, null, null, null);
    }
    
    public Block(BlockType type, String hash, JsonObject jsonRepresentation, String signature,
                 WorkSolution workSolution) {
        if (hash != null && !JNanoHelper.isValidHex(hash, 64))
            throw new IllegalArgumentException("Hash string is invalid.");
        if (!JNanoHelper.isValidHex(signature, 128))
            throw new IllegalArgumentException("Block signature is invalid.");
        
        this.type = type;
        this.hash = hash != null ? hash.toUpperCase() : null;
        this.jsonRepresentation = jsonRepresentation;
        this.signature = signature;
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
                    hash = JNanoHelper.ENCODER_HEX.encode(generateHashBytes());
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
    
    private byte[] generateHashBytes() {
        if (hashBytes == null) {
            synchronized (this) {
                if (hashBytes == null) {
                    if (hash != null) {
                        // Decode from existing hash string
                        hashBytes = JNanoHelper.ENCODER_HEX.decode(hash);
                    } else {
                        hashBytes = calculateHashBytes();
                    }
                }
            }
        }
        return hashBytes;
    }
    
    @Override
    public final BlockType getType() {
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
    
    
    /**
     * @return an array of hashable byte arrays in the correct sequence, or null if not supported
     */
    protected abstract byte[][] generateHashables();
    
    protected final byte[] calculateHashBytes() {
        byte[][] hashables = generateHashables();
        if (hashables == null)
            return null;
    
        Blake2b digest = new Blake2b(null, 32, null, null);
        for (byte[] ba : hashables)
            digest.update(ba, 0, ba.length);
        byte[] hashBytes = new byte[32];
        digest.digest(hashBytes, 0);
        
        return hashBytes;
    }
    
    
    /**
     * @return a JSON representation of this block
     */
    public final String toJsonString() {
        return getJsonObject().toString();
    }
    
    /**
     * @return a JSON representation of this block, as a Gson {@link JsonObject}
     */
    public final JsonObject getJsonObject() {
        if (jsonRepresentation == null) {
            synchronized (this) {
                if (jsonRepresentation == null) {
                    jsonRepresentation = JNanoHelper.GSON.toJsonTree(this).getAsJsonObject();
                }
            }
        }
        return jsonRepresentation;
    }
    
    
    @Override
    public String toString() {
        return this.toJsonString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block)o;
        return Arrays.equals(generateHashBytes(), block.generateHashBytes());
    }
    
    @Override
    public int hashCode() {
        return getHash().hashCode();
    }
    
    /**
     * Parses a block from a given JSON string.
     * @param json the json to parse from
     * @return a block object derived from the provided JSON
     */
    public static Block parse(String json) {
        return BLOCK_DESERIALIZER.deserialize(JsonParser.parseString(json).getAsJsonObject());
    }
    
}
