package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

public interface IBlock {
    
    /**
     * @return the block type
     */
    BlockType getType();
    
    /**
     * @return the block hash
     */
    String getHash();
    
    /**
     * @return the signature which verifies and authorizes this block (may be null)
     */
    String getSignature();
    
    /**
     * @return the work solution (may be null)
     */
    WorkSolution getWorkSolution();
    
}
