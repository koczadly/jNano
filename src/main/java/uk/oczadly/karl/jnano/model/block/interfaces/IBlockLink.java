/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.LinkData;

/**
 * This interface is to be implemented by blocks which contain link data.
 */
public interface IBlockLink extends IBlock {
    
    /**
     * Returns the miscellaneous link data field, encoded as a Nano account. The correct encoding is dependent upon
     * the context and intent of the block.
     * @return the link data, encoded as a Nano account
     *
     * @deprecated Use of {@link #getLink()} is preferred
     */
    @Deprecated
    default NanoAccount getLinkAsAccount() {
        return getLink().asAccount();
    }
    
    /**
     * Returns the miscellaneous link data field, encoded as a hexadecimal string. The correct encoding is dependent
     * upon the context and intent of the block.
     * @return the link data, encoded as a hexadecimal string
     *
     * @deprecated Use of {@link #getLink()} is preferred
     */
    @Deprecated
    default HexData getLinkData() {
        return getLink().asHex();
    }
    
    /**
     * Returns the miscellaneous link data field.
     * <p>The intended purpose and format of the link value can be determined by calling {@link LinkData#getIntent()}
     * on the object.</p>
     * @return the link data
     */
    LinkData getLink();
    
}
