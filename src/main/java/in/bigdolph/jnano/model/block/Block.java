package in.bigdolph.jnano.model.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    
    
    public Block(BlockType type) {
        this.type = type;
    }
    
    public Block(BlockType type, String signature, String workSolution) {
        this.type = type;
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
        return jsonRepresentation;
    }
    
    public void setJsonRepresentation(String json) {
        if(this.jsonRepresentation != null) throw new IllegalStateException("JSON representation has already been set");
        this.jsonRepresentation = json;
    }
    
    
    @Override
    public String toString() {
        if(jsonRepresentation != null) return jsonRepresentation;
        return "{\"type\":\"" + type.toString() + "\"}";
    }
}
