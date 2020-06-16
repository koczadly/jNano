package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.NanoAccount;

/**
 * This interface is to be implemented by blocks which contain a source block hash.
 */
public interface IBlockSource extends IBlockLink {
    
    /**
     * @return the hash of the source block which sent the funds
     */
    String getSourceBlockHash();
    
    
    @Override
    default NanoAccount getLinkAsAccount() {
        return NanoAccount.parsePublicKey(getSourceBlockHash());
    }
    
    @Override
    default String getLinkData() {
        return getSourceBlockHash();
    }

}
