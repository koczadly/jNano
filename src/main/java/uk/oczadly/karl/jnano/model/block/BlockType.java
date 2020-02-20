package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents the types of block available. Only {@link #STATE} blocks should be used from hereon, and have
 * since been deprecated on the official Nano node.
 */
public enum BlockType {
    
    @SerializedName("open")
    @Deprecated
    OPEN    (true),
    
    @SerializedName("change")
    @Deprecated
    CHANGE  (false),
    
    @SerializedName("send")
    @Deprecated
    SEND    (true),
    
    @SerializedName("receive")
    @Deprecated
    RECEIVE (true),
    
    @SerializedName("state")
    STATE   (true);
    
    
    boolean isTransaction;
    
    BlockType(boolean isTransaction) {
        this.isTransaction = isTransaction;
    }
    
    
    /**
     * @return whether the block type represents a transfer of funds
     */
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return the official protocol name of this subtype
     */
    public String getProtocolName() {
        return name().toLowerCase();
    }
    
    
    @Override
    public String toString() {
        return getProtocolName();
    }
    
}
