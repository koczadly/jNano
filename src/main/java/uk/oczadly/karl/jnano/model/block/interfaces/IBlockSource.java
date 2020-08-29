/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

/**
 * This interface is to be implemented by blocks which contain a source block hash.
 */
public interface IBlockSource extends IBlock {
    
    /**
     * @return the hash of the source block which sent the funds
     */
    String getSourceBlockHash();
    
}
