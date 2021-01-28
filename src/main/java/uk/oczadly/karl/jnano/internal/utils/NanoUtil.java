/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.block.Block;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockAccount;
import uk.oczadly.karl.jnano.model.block.interfaces.IBlockPrevious;

/**
 * Helper class
 */
public class NanoUtil {
    
    public static HexData getWorkRoot(Block block) {
        if (block == null)
            throw new IllegalArgumentException("Block cannot be null.");
        
        // Try 'previous'
        if (block instanceof IBlockPrevious) {
            HexData previous = ((IBlockPrevious)block).getPreviousBlockHash();
            if (!previous.isZero())
                return previous;
        }
        // Try 'account'
        if (block instanceof IBlockAccount) {
            return new HexData(((IBlockAccount)block).getAccount().toPublicKey());
        }
        throw new IllegalArgumentException("The root hash cannot be determined from the given block.");
    }

}
