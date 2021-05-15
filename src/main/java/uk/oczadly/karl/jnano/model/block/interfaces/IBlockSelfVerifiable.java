/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model.block.interfaces;

/**
 * This interface is to be implemented by blocks which contain a representative address.
 */
public interface IBlockSelfVerifiable extends IBlock {
    
    /**
     * Tests whether the signature is valid and was signed by the correct account. For implementations which rely on a
     * specific network, the Nano Live network will be used.
     *
     * @return true if the signature matches, false if not <em>or</em> if the {@code signature} is currently null
     * @throws SignatureVerificationException if there's not enough information to verify the signature (eg.
     *                                        unrecognized epoch)
     */
    boolean verifySignature();
    
    
    final class SignatureVerificationException extends RuntimeException {
        public SignatureVerificationException(String message) {
            super(message);
        }
    
        public SignatureVerificationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
