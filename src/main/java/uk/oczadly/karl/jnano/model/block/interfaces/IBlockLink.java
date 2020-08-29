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
     * @return the link data, encoded as a Nano account
     */
    NanoAccount getLinkAsAccount();
    
    /**
     * @return the link data, encoded as a hexadecimal string
     */
    String getLinkData();

}
