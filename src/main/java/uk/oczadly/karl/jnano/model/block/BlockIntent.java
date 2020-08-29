package uk.oczadly.karl.jnano.model.block;

public class BlockIntent {
    
    private final UncertainBool isSend, isReceive, isChange, isOpen, isEpoch;
    
    public BlockIntent(Boolean isSend, Boolean isReceive, Boolean isChange, Boolean isOpen, Boolean isEpoch) {
        this(UncertainBool.valueOf(isSend), UncertainBool.valueOf(isReceive), UncertainBool.valueOf(isChange),
                UncertainBool.valueOf(isOpen), UncertainBool.valueOf(isEpoch));
    }
    
    public BlockIntent(UncertainBool isSend, UncertainBool isReceive, UncertainBool isChange, UncertainBool isOpen,
                       UncertainBool isEpoch) {
        this.isSend = UncertainBool.notNull(isSend);
        this.isReceive = UncertainBool.notNull(isReceive);
        this.isOpen = UncertainBool.notNull(isOpen);
        this.isChange = UncertainBool.anyOf(UncertainBool.notNull(isChange), this.isOpen); // Open overrides rep change
        this.isEpoch = UncertainBool.notNull(isEpoch);
    }
    
    
    /**
     * @return true if the block sent funds
     */
    public UncertainBool isSendFunds() {
        return isSend;
    }
    
    /**
     * @return true if the block received funds
     */
    public UncertainBool isReceiveFunds() {
        return isReceive;
    }
    
    /**
     * @return true if the block changed the representative, or if the account is new
     */
    public UncertainBool isChangeRep() {
        return isChange;
    }
    
    /**
     * @return true if this is the opening block for the account
     */
    public UncertainBool isFirstBlock() {
        return isOpen;
    }
    
    /**
     * @return true if this block is an account upgrade block
     */
    public UncertainBool isEpochUpgrade() {
        return isEpoch;
    }
    
    /**
     * Returns whether this block has a transactional function. Note that this is not mututally exclusive to
     * {@link #isSpecial()} returning true.
     * @return true if this block has a transactional function
     */
    public UncertainBool isTransactional() {
        return UncertainBool.anyOf(isSend, isReceive);
    }
    
    /**
     * Returns whether this block has a special (non-transactional) function. Note that this is not mututally
     * exclusive to {@link #isTransactional()} returning true.
     * @return true if this block has a non-transactional function
     */
    public UncertainBool isSpecial() {
        return UncertainBool.anyOf(isEpoch, isChange);
    }
    
    /**
     * Returns whether this block has a functional purpose. Some block may exist on the network, but have no practical
     * functionality or action.
     * @return true if this block has a function
     */
    public UncertainBool hasPurpose() {
        return UncertainBool.anyOf(isSend, isReceive, isChange, isOpen, isEpoch);
    }
    
    
    @Override
    public String toString() {
        return "BlockIntent{" +
                "isSendFunds=" + isSendFunds() +
                ", isReceiveFunds=" + isReceiveFunds() +
                ", isChangeRep=" + isChangeRep() +
                ", isFirstBlock=" + isFirstBlock() +
                ", isEpochUpgrade=" + isEpochUpgrade() +
                ", isTransactional=" + isTransactional() +
                ", isSpecial=" + isSpecial() +
                ", hasPurpose=" + hasPurpose() + '}';
    }
    
    
    /**
     * Represents a boolean state value, with the addition of the {@link #UNKNOWN} value in cases of uncertainty. In
     * most cases, calling {@link #bool()} on the value will suffice.
     */
    public enum UncertainBool {
        /** Equivalent to {@code true}. */
        TRUE,
        /** Equivalent to {@code false}. */
        FALSE,
        /** If a value is unknown or uncertain from the provided context. Treat this value as "it's a possibility". */
        UNKNOWN;
        
    
        /**
         * @return true if this == {@link #TRUE}, or false if {@link #FALSE} or {@link #UNKNOWN}
         */
        public boolean bool() {
            return this == TRUE;
        }
    
        /**
         * @param bool the boolean to convert from
         * @return {@link #TRUE} if true, {@link #FALSE} if false, {@link #UNKNOWN} if null
         */
        public static UncertainBool valueOf(java.lang.Boolean bool) {
            if (bool == null) return UNKNOWN;
            return bool ? TRUE : FALSE;
        }
    
        /**
         * Returns {@link #TRUE} whenever {@code bool} is true, {@link #FALSE} when {@code bool} is false and
         * {@code isKnown} is true, and {@link #UNKNOWN} when {@code bool} is false and {@code isKnown} is false.
         * @param isKnown if the value is known
         * @param bool    the value to convert
         * @return the enum value
         */
        public static UncertainBool ifKnown(boolean isKnown, boolean bool) {
            return bool ? TRUE : (isKnown ? FALSE : UNKNOWN);
        }
    
        /**
         * Returns {@link #TRUE} if any of the values are {@code true}, {@link #FALSE} if all the values are
         * {@code false}, and {@link #UNKNOWN} if at least one value is {@code unknown} and all others are false.
         * @param bools an array of UncertainBool values
         * @return the calculated UncertainBool value
         */
        public static UncertainBool anyOf(UncertainBool...bools) {
            boolean anyUnknown = false;
            for (UncertainBool state : bools) {
                if (state == TRUE) {
                    return TRUE;
                } else if (state == UNKNOWN) {
                    anyUnknown = true;
                }
            }
            return anyUnknown ? UNKNOWN : FALSE;
        }
        
        static UncertainBool notNull(UncertainBool bool) {
            return bool == null ? UNKNOWN : bool;
        }
    }
    
}
