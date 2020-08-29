/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import java.math.BigInteger;

/**
 * This interface is to be implemented by blocks which contain the account's balance.
 */
public interface IBlockBalance extends IBlock {
    
    /**
     * Returns the remaining balance of the account <em>after</em> this block has been processed.
     * @return the balance of the account after this transaction
     */
    BigInteger getBalance();

}
