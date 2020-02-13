package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

public enum BlockType {
    
    @SerializedName("open")
    OPEN    ("open",    true),
    
    @SerializedName("change")
    CHANGE  ("change",  false),
    
    @SerializedName("send")
    SEND    ("send",    true),
    
    @SerializedName("receive")
    RECEIVE ("receive", true),
    
    @SerializedName("state")
    STATE   ("state",   true); // TODO block classes may need re-working
    
    
    private String name;
    private boolean isTransaction;
    
    BlockType(String name, boolean isTransaction) {
        this.name = name;
        this.isTransaction = isTransaction;
    }
    
    
    
    public boolean isTransaction() {
        return isTransaction;
    }
    
    public String getProtocolName() {
        return name;
    }
    
    
    @Override
    public String toString() {
        return this.getProtocolName();
    }
    
}
