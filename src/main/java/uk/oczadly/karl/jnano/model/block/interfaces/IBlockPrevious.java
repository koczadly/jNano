/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

/**
 * This interface is to be implemented by blocks which contain a previous block hash.
 */
public interface IBlockPrevious extends IBlock {
    
    /**
     * @return the hash of the previous block in this account's blockchain
     */
    String getPreviousBlockHash();

}
