package in.bigdolph.jnano.model.block;

import com.google.gson.annotations.SerializedName;

public enum BlockType {
    
    @SerializedName("open")
    OPEN,
    
    @SerializedName("change")
    CHANGE,
    
    @SerializedName("send")
    SEND,
    
    @SerializedName("receive")
    RECEIVE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
}
