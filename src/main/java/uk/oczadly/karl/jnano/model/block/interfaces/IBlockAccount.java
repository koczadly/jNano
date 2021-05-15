/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain the owning account.
 */
public interface IBlockAccount extends IBlock, IBlockSelfVerifiable {
    
    /**
     * Returns the account which this block belongs to.
     * @return the account
     */
    NanoAccount getAccount();
    
    /**
     * Tests whether the signature is valid and was signed by the correct account.
     *
     * @return true if the signature is correct, false if not <em>or</em> if the {@code signature} is currently null
     */
    @Override
    default boolean verifySignature() {
        return verifySignature(getAccount());
    }
    
}
