/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.HexData;

/**
 * This interface is to be implemented by blocks which contain a source block hash.
 */
public interface IBlockSource extends IBlock {
    
    /**
     * Returns the hash of the block which this block is interacting with (eg. a {@code receive} block accepting the
     * funds sent from the source {@code send} block).
     * @return the hash of the source block
     */
    HexData getSourceBlockHash();
    
}
