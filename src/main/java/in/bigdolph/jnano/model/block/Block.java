package in.bigdolph.jnano.model.block;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
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
    private boolean representationSet;
    
    
    protected Block(BlockType type) {
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
        if(jsonRepresentation == null) jsonRepresentation = new GsonBuilder().create().toJson(this);
        return jsonRepresentation;
    }
    
    /** This method may only be called prior to any getJsonRepresentation requests */
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
