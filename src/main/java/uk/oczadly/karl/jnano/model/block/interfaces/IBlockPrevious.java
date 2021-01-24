/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.HexData;

/**
 * This interface is to be implemented by blocks which contain a previous block hash.
 */
public interface IBlockPrevious extends IBlock {
    
    /**
     * Returns the hash of the previous block in the account's chain.
     * @return the previous block hash
     */
    HexData getPreviousBlockHash();

}
