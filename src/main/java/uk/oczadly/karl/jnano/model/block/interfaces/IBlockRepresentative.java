package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain a representative address.
 */
public interface IBlockRepresentative {
    
    /**
     * @return the representative address for this account
     */
    NanoAccount getRepresentative();

}
