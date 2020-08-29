/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain link data.
 */
public interface IBlockLink extends IBlock {
    
    /**
     * Returns the miscellaneous link data field, encoded as a Nano account. The correct encoding is dependent upon
     * the context and intent of the block.
     * @return the link data, encoded as a Nano account
     */
    NanoAccount getLinkAsAccount();
    
    /**
     * Returns the miscellaneous link data field, encoded as a hexadecimal string. The correct encoding is dependent
     * upon the context and intent of the block.
     * @return the link data, encoded as a hexadecimal string
     */
    String getLinkData();

}
