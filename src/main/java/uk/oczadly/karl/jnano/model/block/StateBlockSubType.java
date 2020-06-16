package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains enum instances which each represent a sub-type of state block.
 */
public enum StateBlockSubType {
    
    @SerializedName("send")
    SEND("send", true),
    
    @SerializedName("receive")
    RECEIVE("receive", true),
    
    @SerializedName("change")
    CHANGE_REP("change", false),
    
    @SerializedName("epoch")
    EPOCH("epoch", false);
    
    
    static final Map<String, StateBlockSubType> nameLookupMap = new HashMap<>();
    
    static {
        for (StateBlockSubType type : values())
            nameLookupMap.put(type.getProtocolName().toLowerCase(), type);
    }
    
    
    String name;
    boolean isTransaction;
    
    StateBlockSubType(String name, boolean isTransaction) {
        this.name = name;
        this.isTransaction = isTransaction;
    }
    
    
    /**
     * @return whether the block (sub)type represents a transfer of funds
     */
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return the official protocol name of this subtype
     */
    public String getProtocolName() {
        return name;
    }
    
    
    @Override
    public String toString() {
        return getProtocolName();
    }
    
    
    /**
     * Returns the enum constant of this type corresponding to the legacy {@code BlockType}.
     *
     * @param type the legacy block type
     * @return the corresponding subtype, or null if incompatible
     */
    @SuppressWarnings("deprecation")
    public static StateBlockSubType getFromLegacyType(BlockType type) {
        switch (type) {
            case SEND:
                return SEND;
            case RECEIVE:
            case OPEN:
                return RECEIVE;
            case CHANGE:
                return CHANGE_REP;
        }
        return null;
    }
    
    /**
     * Returns the enum constant of this type from the given official name.
     *
     * @param name the official type name
     * @return the corresponding subtype, or null if not found
     */
    public static StateBlockSubType getFromName(String name) {
        return nameLookupMap.get(name.toLowerCase());
    }
    
}
