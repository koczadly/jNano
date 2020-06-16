package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.StateBlock;
import uk.oczadly.karl.jnano.model.block.StateBlockSubType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum AccountEpoch {
    
    /**
     * Version 1 epoch block, which marks the transition from legacy blocks (send/receive/open/change) to UTX state
     * blocks.
     */
    V1(1, "65706F636820763120626C6F636B000000000000000000000000000000000000",
            NanoAccount.parseSegment("3t6k35gi95xu6tergt6p69ck76ogmitsa8mnijtpxm9fkcm736xt")),
    
    /**
     * Version 2 epoch block, which marks the change in minimum work difficulties introduced in node V21.
     */
    V2(2, "65706F636820763220626C6F636B000000000000000000000000000000000000",
            NanoAccount.parseSegment("3qb6o6i1tkzr6jwr5s7eehfxwg9x6eemitdinbpi7u8bjjwsgqfj"));
    
    
    public static final AccountEpoch LATEST_EPOCH = values()[values().length - 1];
    
    private static final Map<Integer, AccountEpoch> VER_MAP = new HashMap<>();
    private static final Map<String, AccountEpoch> ID_MAP = new HashMap<>();
    
    static {
        for (AccountEpoch e : AccountEpoch.values()) {
            VER_MAP.put(e.version, e);
            ID_MAP.put(e.id, e);
        }
    }
    
    int version;
    String id;
    NanoAccount signerAcc;
    
    AccountEpoch(int version, String id, NanoAccount signerAcc) {
        this.version = version;
        this.id = id;
        this.signerAcc = signerAcc;
    }
    
    
    /**
     * @return the version which this epoch block will upgrade an account to
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * @return the identifier (block {@code link} data) of this epoch upgrade
     */
    public String getIdentifier() {
        return id;
    }
    
    /**
     * @return the account used to sign the epoch block
     */
    public NanoAccount getSignerAccount() {
        return signerAcc;
    }
    
    
    /**
     * Returns the appropriate epoch transition from a given version integer.
     * @param ver the version which the epoch block will upgrade the account to
     * @return the related epoch transition, or null if not found
     */
    public static AccountEpoch fromVersion(int ver) {
        return VER_MAP.get(ver);
    }
    
    /**
     * Returns the appropriate epoch transition from a given identifier.
     * @param id the identifier of the epoch transition (link data value)
     * @return the related epoch transition, or null if not found
     */
    public static AccountEpoch fromIdentifier(String id) {
        return ID_MAP.get(id.toUpperCase());
    }
    
    /**
     * Retrieves the corresponding {@link AccountEpoch} from a given epoch block. In cases where the given block is
     * not an epoch block, this method will return null.
     * @param block the epoch block
     * @return the epoch version which this block represents, or null if the block isn't an epoch block
     */
    public static AccountEpoch fromEpochBlock(Block block) {
        if (block instanceof StateBlock) {
            StateBlock sb = (StateBlock)block;
            if (sb.getSubType() == null || sb.getSubType() == StateBlockSubType.EPOCH) {
                AccountEpoch epoch = fromIdentifier(sb.getLinkData());
                if (epoch == null)
                    throw new IllegalArgumentException("Unknown epoch identifier " + sb.getLinkData() + ".");
                return epoch;
            }
        }
        return null;
    }
    
    /**
     * Calculates the account version from a given set of blocks. This method will ignore all non-epoch blocks, and
     * return the largest one found in the list. In cases where no epoch block is present, the latest epoch block
     * will be returned.
     * @param blocks a set of blocks within a certain account
     * @return the latest epoch block in the list, or the latest epoch if none are found
     */
    public static AccountEpoch calculateAccountVersion(Collection<Block> blocks) {
        AccountEpoch latestEpoch = null;
        for (Block b : blocks) {
            // Manual checks
            if (latestEpoch == null && b instanceof StateBlock) latestEpoch = V1; // Upgrade to state blocks
            
            // Check if b is an epoch block
            AccountEpoch epoch = fromEpochBlock(b);
            if (epoch != null && (latestEpoch == null || epoch.compareTo(latestEpoch) > 0))
                latestEpoch = epoch;
        }
        return latestEpoch != null ? latestEpoch : LATEST_EPOCH;
    }
    
}
