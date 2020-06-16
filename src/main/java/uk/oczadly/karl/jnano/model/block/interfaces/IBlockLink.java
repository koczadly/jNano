package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain link data.
 */
public interface IBlockLink {
    
    /**
     * @return the link data, encoded as a Nano account
     */
    NanoAccount getLinkAsAccount();
    
    /**
     * @return the link data, encoded as a hexadecimal string
     */
    String getLinkData();

}
