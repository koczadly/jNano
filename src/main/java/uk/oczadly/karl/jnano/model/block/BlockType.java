package uk.oczadly.karl.jnano.model.block;

import com.google.gson.annotations.SerializedName;

import java.util.*;

/**
 * This class represents the types of block available. Only {@link #STATE} blocks should be used from hereon, and have
 * since been deprecated on the official Nano node.
 */
@SuppressWarnings("DeprecatedIsStillUsed")
public enum BlockType {
    
    @SerializedName("open")
    OPEN    (true,  OpenBlock.class),
    
    @SerializedName("change")
    CHANGE  (false, ChangeBlock.class),
    
    @SerializedName("send")
    SEND    (true,  SendBlock.class),
    
    @SerializedName("receive")
    RECEIVE (true,  ReceiveBlock.class),
    
    @SerializedName(value = "state", alternate = "utx")
    STATE   (true,  StateBlock.class, new String[] {"utx"});
    
    
    private static final Map<String, BlockType> LOOKUP_MAP = new HashMap<>();
    
    static {
        for (BlockType type : BlockType.values()) {
            LOOKUP_MAP.put(type.getProtocolName().toLowerCase(), type);
            type.getAlternateNames().forEach(n -> LOOKUP_MAP.put(n.toLowerCase(), type));
        }
    }
    
    
    boolean isTransaction;
    Class<? extends Block> blockClass;
    Set<String> altNames;
    
    BlockType(boolean isTransaction, Class<? extends Block> blockClass) {
        this(isTransaction, blockClass, new String[0]);
    }
    
    BlockType(boolean isTransaction, Class<? extends Block> blockClass, String[] altNames) {
        this.isTransaction = isTransaction;
        this.blockClass = blockClass;
        this.altNames = Collections.unmodifiableSet(new HashSet<>(List.of(altNames)));
    }
    
    
    /**
     * @return whether the block type represents a transfer of funds
     * @deprecated Certain block types require contextual information to determine the action. This will return
     * {@code true} for state blocks, but this is not necessarily the case.
     */
    @Deprecated
    public boolean isTransaction() {
        return isTransaction;
    }
    
    /**
     * @return the class associated with the block type
     */
    public Class<? extends Block> getBlockClass() {
        return blockClass;
    }
    
    /**
     * @return the official protocol name of this subtype
     */
    public String getProtocolName() {
        return name().toLowerCase();
    }
    
    /**
     * @return a list of alternate protocol names
     */
    public Set<String> getAlternateNames() {
        return altNames;
    }
    
    @Override
    public String toString() {
        return getProtocolName();
    }
    
    
    /**
     * Returns the enum constant of this type from the protocol name.
     *
     * @param name the type name
     * @return the corresponding type, or null if not found
     */
    public static BlockType fromName(String name) {
        return LOOKUP_MAP.get(name.toLowerCase());
    }
    
}
