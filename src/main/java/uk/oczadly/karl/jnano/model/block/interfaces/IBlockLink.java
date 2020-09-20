/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain link data.
 */
public interface IBlockLink extends IBlock {
    
    /**
     * Returns the miscellaneous link data field, encoded as a Nano account. The correct encoding is dependent upon
     * the context and intent of the block.
     * @return the link data, encoded as a Nano account
     * @see #getLinkType()
     */
    NanoAccount getLinkAsAccount();
    
    /**
     * Returns the miscellaneous link data field, encoded as a hexadecimal string. The correct encoding is dependent
     * upon the context and intent of the block.
     * @return the link data, encoded as a hexadecimal string
     * @see #getLinkType()
     */
    String getLinkData();
    
    /**
     * Returns the type of data encoded by the link field in this block.
     * <p>To determine the intended encoding type, call {@code getFormat()} on the {@link LinkType} returned by this
     * method.</p>
     * @return the link format for this block
     */
    LinkType getLinkType();
    
    
    
    /**
     * This enum contains values which represent the different link formats.
     */
    enum LinkFormat {
        /** Encoded as an address.
         * @see #getLinkAsAccount() */
        ACCOUNT,
        
        /** Encoded as a 64-character hexadecimal string.
         * @see #getLinkData() */
        DATA,
        
        /** The link field is not used for this block. */
        EMPTY;
    
    
        /**
         * Returns the link data of the given block, encoded in this format (as a string).
         * <p>The following formats are used:</p>
         * <ul>
         *     <li>{@link LinkFormat#ACCOUNT} - the full address ({@link NanoAccount#toAddress()})</li>
         *     <li>{@link LinkFormat#DATA} - 64-character hexadecimal</li>
         *     <li>{@link LinkFormat#EMPTY} - 64-character string of zeroes</li>
         * </ul>
         * @param block the block to retrieve the link from
         * @return a string representation of the link data
         */
        public String getBlockLink(IBlockLink block) {
            if (block == null)
                throw new IllegalArgumentException("Block cannot be null.");
    
            switch (this) {
                case ACCOUNT: return block.getLinkAsAccount().toAddress();
                case DATA:    return block.getLinkData();
                case EMPTY:   return JNH.ZEROES_64;
            }
            throw new AssertionError("Unknown link format.");
        }
    }
    
    /**
     * This enum contains values which represent the different link types.
     */
    enum LinkType {
        /** A destination address for an outgoing transaction. */
        DESTINATION         (LinkFormat.ACCOUNT),
        
        /** A hash of the source block for an incoming transaction. */
        SOURCE_HASH         (LinkFormat.DATA),
        
        /** An identification code of an epoch block. */
        EPOCH_IDENTIFIER    (LinkFormat.DATA),
        
        /** The link field is unused. */
        NOT_USED            (LinkFormat.EMPTY);
        
        private final LinkFormat type;
        LinkType(LinkFormat type) {
            this.type = type;
        }
    
        /**
         * @return the intended format of this data field
         */
        public LinkFormat getFormat() {
            return type;
        }
    }

}
