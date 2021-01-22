/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class WalletUtil {
    private WalletUtil() {}
    
    
    /**
     * Generates and returns a new randomly created private key or seed.
     *
     * <p>This method uses the {@link SecureRandom#getInstanceStrong()} random instance to generate the key, which
     * may be expensive to repeatedly call.</p>
     *
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     *
     * @throws NoSuchAlgorithmException if a strong SecureRandom algorithm isn't set or can't be found
     */
    public static HexData generateRandomKey() throws NoSuchAlgorithmException {
        return generateRandomKey(SecureRandom.getInstanceStrong());
    }
    
    /**
     * Generates and returns a new randomly created private key or seed from a given {@link SecureRandom} instance.
     *
     * @param random the random generator used to create the seed
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static HexData generateRandomKey(SecureRandom random) {
        byte[] data = new byte[NanoConst.LEN_KEY_B];
        random.nextBytes(data);
        return new HexData(data);
    }
    
    
    /**
     * Derives a private key from the given seed, using the zeroth index.
     * @param seed the seed, represented as a 64-character hex string
     * @return a 64-character hex value representing the private key
     * @see #deriveKeyFromSeed(String, int) 
     */
    public static HexData deriveKeyFromSeed(String seed) {
        return deriveKeyFromSeed(new HexData(seed), 0);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 64-character hex string
     * @param index the index of the account (as an unsigned integer)
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(String seed, int index) {
        return deriveKeyFromSeed(new HexData(seed), index);
    }
    
    /**
     * Derives a private key from the given seed, using the zeroth index.
     * @param seed  the seed, represented as a 64-character hex object
     * @return a 64-character hex value representing the private key
     * @see #deriveKeyFromSeed(HexData, int) 
     */
    public static HexData deriveKeyFromSeed(HexData seed) {
        return deriveKeyFromSeed(seed, 0);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 64-character hex object
     * @param index the index of the account (as an unsigned integer)
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(HexData seed, int index) {
        if (seed == null)
            throw new IllegalArgumentException("Seed cannot be null.");
        if (seed.length() != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Seed length is invalid.");
        
        byte[] seedBytes = deriveKeyFromSeed(seed.toByteArray(), index);
        return new HexData(seedBytes);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 32-element byte array
     * @param index the index of the account (as an unsigned integer)
     * @return a 64-character hex value representing the private key
     */
    public static byte[] deriveKeyFromSeed(byte[] seed, int index) {
        if (seed == null)
            throw new IllegalArgumentException("Seed cannot be null.");
        if (seed.length != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Seed length is invalid.");
        
        return JNH.blake2b(32, seed, JNH.intToBytes(index));
    }
    
}
