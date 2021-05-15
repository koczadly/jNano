/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.NanoAccount;
import uk.oczadly.karl.jnano.model.block.BlockIntent;
import uk.oczadly.karl.jnano.model.block.BlockType;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

/**
 * This interface is to be implemented by blocks, and contains the required parameters for each block type.
 */
public interface IBlock {
    
    /**
     * Returns the type of block, represented by the {@link BlockType} enum. Note that this value may not be
     * specified in cases where a custom block type is used, and will return null — in these cases, use
     * {@link #getTypeString()}.
     * @return the block type (may be null)
     */
    default BlockType getType() {
        return null;
    }
    
    /**
     * Returns the type of block, represented by a string value.
     * @return the block type, as a string
     */
    default String getTypeString() {
        BlockType type = getType();
        if (type != null) return type.getProtocolName();
        throw new UnsupportedOperationException("This method isn't correctly implemented by the class.");
    }
    
    /**
     * Returns the 64-character hexadecimal hash of this block.
     * @return the block hash
     */
    HexData getHash();
    
    /**
     * Returns the 128-character hexadecimal signature which verifies the ownership and approval of this block by the
     * account owner.
     * @return the signature of this block (may be null)
     */
    HexData getSignature();
    
    /**
     * Returns the work solution of this block.
     * @return the work solution (may be null)
     */
    WorkSolution getWorkSolution();
    
    /**
     * Returns an object containing details on the context and intent of this block.
     * @return the intent of this block
     */
    BlockIntent getIntent();
    
    /**
     * Returns whether or not this object contains all the necessary fields to be a valid block. This includes having
     * non-null work and signature values.
     * @return true if this block contains all mandatory fields
     */
    boolean isComplete();
    
    /**
     * Tests whether the signature is valid and was signed by the specified account.
     *
     * <p>Be aware that some special blocks (such as epoch upgrades) may be signed by an account other than the
     * block's owner. These blocks have their own specific signers and behaviour, and should provide their own
     * method in addition to this for checking the validity of the signature.</p>
     *
     * @param account the signer account (public key) to test
     * @return true if the specified account is the signer of this block's signature, or false if not <em>or</em> if
     *         the {@code signature} field is null
     */
    boolean verifySignature(NanoAccount account);
    
}
