package in.bigdolph.jnano.model.block;

import com.google.gson.annotations.SerializedName;

public enum BlockType {
    
    @SerializedName("open")
    OPEN(true),
    
    @SerializedName("change")
    CHANGE(false),
    
    @SerializedName("send")
    SEND(true),
    
    @SerializedName("receive")
    RECEIVE(true);
    
    
    private final String name = this.name().toLowerCase();
    private final boolean isTransaction;
    
    BlockType(boolean isTransaction) {
        this.isTransaction = isTransaction;
    }
    
    
    
    public boolean isTransaction() {
        return isTransaction;
    }
    
    
    @Override
    public String toString() {
        return this.name;
    }
    
}
