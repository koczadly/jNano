/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.block.factory.AccountState;

/**
 * This interface is to be implemented by blocks which contain the entire account state.
 */
public interface IBlockState extends IBlock {
    
    /**
     * Returns the state of the account chain at the point in time of this block.
     * @return the account state
     */
    AccountState getAccountState();

}
