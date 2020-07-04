package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

/**
 * This class represents the types of block available. Only {@link #STATE} blocks should be used from hereon, and have
 * since been deprecated on the official Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
public enum BlockType {
    
    @SerializedName("open")
    OPEN(true),
    
    @SerializedName("change")
    CHANGE(false),
    
    @SerializedName("send")
    SEND(true),
    
    @SerializedName("receive")
    RECEIVE(true),
    
    @SerializedName("state")
    STATE(true);
    
    
    boolean isTransaction;
    
    BlockType(boolean isTransaction) {
        this.isTransaction = isTransaction;
    }
    
    
    /**
     * @return whether the block type represents a transfer of funds
     * @deprecated Certain block types require contextual information to determine the action.
     */
    @Deprecated
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
