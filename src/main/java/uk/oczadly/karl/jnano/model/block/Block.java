package uk.oczadly.karl.jnano.model.block;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.gsonadapters.BlockTypeDeserializer;

@JsonAdapter(BlockTypeDeserializer.class)
public abstract class Block {
    
    @Expose
    @SerializedName("hash")
    private String hash;
    
    @Expose
    @SerializedName("type")
    private final BlockType type;
    
    @Expose
    @SerializedName("signature")
    private String signature;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    private JsonObject jsonRepresentation;
    
    
    protected Block(BlockType type) {
        this.type = type;
    }
    
    public Block(BlockType type, String hash, JsonObject jsonRepresentation, String signature, String workSolution) {
        this.type = type;
        this.hash = hash;
        this.jsonRepresentation = jsonRepresentation;
        this.signature = signature;
        this.workSolution = workSolution;
    }
    
    
    public final String getHash() {
        return hash;
    }
    
    public final BlockType getType() {
        return type;
    }
    
    public final String getSignature() {
        return signature;
    }
    
    public final String getWorkSolution() {
        return workSolution;
    }
    
    
    public String getJsonString() {
        return getJsonObject().toString();
    }
    
    public JsonObject getJsonObject() {
        if(jsonRepresentation == null)
            jsonRepresentation = new GsonBuilder().create().toJsonTree(this).getAsJsonObject();
        return jsonRepresentation;
    }
    
    
    @Override
    public String toString() {
        return this.getJsonString();
    }
}
