package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain the owning account.
 */
public interface IBlockAccount extends IBlock {
    
    /**
     * @return the account which this block belongs to
     */
    NanoAccount getAccount();

}
