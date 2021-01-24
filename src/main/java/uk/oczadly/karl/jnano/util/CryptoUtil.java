/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.Ed25519Blake2b;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;

/**
 * Low-level operations for hashing, signing and verifying data.
 */
public final class CryptoUtil {
    private CryptoUtil() {}
    
    
    /**
     * Signs a set of data with the given private key, and returns the generated signature.
     * @param data       the data to sign
     * @param privateKey the private key (as a byte array)
     * @return the signature
     */
    public static HexData sign(byte[] data, byte[] privateKey) {
        return sign(new byte[][] { data }, privateKey);
    }
    
    /**
     * Signs a set of data with the given private key, and returns the generated signature.
     *
     * @param data       the data to sign
     * @param privateKey the private key (as a byte array)
     * @return the signature
     */
    public static HexData sign(byte[][] data, byte[] privateKey) {
        if (data == null)
            throw new IllegalArgumentException("Data array cannot be null.");
        if (privateKey == null)
            throw new IllegalArgumentException("Private key cannot be null.");
        if (privateKey.length != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Private key length is invalid.");
        
        byte[] sig = Ed25519Blake2b.sign(privateKey, data);
        return new HexData(sig, NanoConst.LEN_SIGNATURE_B);
    }
    
    /**
     * Verifies whether the signature matches the data and public key.
     * @param data      the data to sign
     * @param signature the signature (as a 64-length byte array)
     * @param publicKey the public key (as a 32-length byte array)
     * @return true if the signature matches, false if it doesn't <em>or</em> if the public key is an invalid Ed25519
     *         key
     */
    public static boolean verifySig(byte[] data, byte[] signature, byte[] publicKey) {
        return verifySig(new byte[][] { data }, signature, publicKey);
    }
    
    /**
     * Verifies whether the signature matches the data and public key.
     * @param data      the data to sign
     * @param signature the signature (as a 64-length byte array)
     * @param publicKey the public key (as a 32-length byte array)
     * @return true if the signature matches, false if it doesn't <em>or</em> if the public key is an invalid Ed25519
     *         key
     */
    public static boolean verifySig(byte[][] data, byte[] signature, byte[] publicKey) {
        if (data == null)
            throw new IllegalArgumentException("Data array cannot be null.");
        if (signature == null)
            throw new IllegalArgumentException("Signature cannot be null.");
        if (signature.length != NanoConst.LEN_SIGNATURE_B)
            throw new IllegalArgumentException("Signature length is invalid.");
        if (publicKey == null)
            throw new IllegalArgumentException("Public key cannot be null.");
        if (publicKey.length != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Public key length is invalid.");
        
        return Ed25519Blake2b.verify(publicKey, data, signature);
    }
    
    
    /**
     * Hashes the given byte array using the {@code Blake2b} message digest algorithm, with a digest output size of 32
     * bytes.
     *
     * @param data the data to hash
     * @return the hash digest, as an array of 32 bytes
     */
    public static HexData hash(byte[]... data) {
        return hash(NanoConst.LEN_HASH_B, data);
    }
    
    /**
     * Hashes the given byte array using the {@code Blake2b} message digest algorithm, with the specified output size.
     *
     * @param outputSize the size of the output in bytes
     * @param data       the data to hash
     * @return the hash digest, as an array of bytes
     */
    public static HexData hash(int outputSize, byte[]... data) {
        return new HexData(JNH.blake2b(outputSize, data), outputSize);
    }
    
}
