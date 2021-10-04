/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain the owning account.
 */
public interface IBlockAccount extends IBlock {
    
    /**
     * Returns the account which this block belongs to.
     * @return the account
     */
    NanoAccount getAccount();
    
}
