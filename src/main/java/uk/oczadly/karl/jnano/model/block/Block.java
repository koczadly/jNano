package uk.oczadly.karl.jnano.model.block;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import uk.oczadly.karl.jnano.rpc.adapters.BlockTypeDeserializer;

@JsonAdapter(BlockTypeDeserializer.class)
public abstract class Block {
    
    @Expose
    @SerializedName("type")
    private final BlockType type;
    
    @Expose
    @SerializedName("signature")
    private String signature;
    
    @Expose
    @SerializedName("work")
    private String workSolution;
    
    private String jsonRepresentation;
    
    
    protected Block(BlockType type) {
        this.type = type;
    }
    
    public Block(BlockType type, String jsonRepresentation, String signature, String workSolution) {
        this.type = type;
        this.jsonRepresentation = jsonRepresentation;
        this.signature = signature;
        this.workSolution = workSolution;
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
    
    
    public String getJsonRepresentation() {
        if(jsonRepresentation == null) jsonRepresentation = new GsonBuilder().create().toJson(this);
        return jsonRepresentation;
    }
    
    
    @Override
    public String toString() {
        return this.getJsonRepresentation();
    }
}
