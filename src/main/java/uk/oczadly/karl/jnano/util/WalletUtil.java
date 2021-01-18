/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;

import java.security.SecureRandom;

public final class WalletUtil {
    private WalletUtil() {}
    
    public static final long MAX_SEED_INDEX = 4294967295L;
    
    
    /**
     * Generates and returns a new randomly created seed (or private key).
     *
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static HexData generateRandomSeed() {
        return generateRandomSeed(new SecureRandom());
    }
    
    /**
     * Generates and returns a new randomly created seed (or private key) from a given {@link SecureRandom} instance.
     *
     * @param random the random generator used to create the seed
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static HexData generateRandomSeed(SecureRandom random) {
        byte[] data = new byte[NanoConst.LEN_KEY_B];
        random.nextBytes(data);
        return new HexData(data);
    }
    
    
    /**
     * Derives a private key from the given seed, using the zeroth index.
     * @param seed the seed, represented as a 64-character hex string
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(String seed) {
        return deriveKeyFromSeed(new HexData(seed), 0L);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 64-character hex string
     * @param index the index of the account (from {@code 0} to {@code 4294967295})
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(String seed, long index) {
        return deriveKeyFromSeed(new HexData(seed), index);
    }
    
    /**
     * Derives a private key from the given seed, using the zeroth index.
     * @param seed  the seed, represented as a 64-character hex object
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(HexData seed) {
        return deriveKeyFromSeed(seed, 0L);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 64-character hex object
     * @param index the index of the account (from {@code 0} to {@code 4294967295})
     * @return a 64-character hex value representing the private key
     */
    public static HexData deriveKeyFromSeed(HexData seed, long index) {
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
     * @param index the index of the account (from {@code 0} to {@code 4294967295})
     * @return a 64-character hex value representing the private key
     */
    public static byte[] deriveKeyFromSeed(byte[] seed, long index) {
        if (seed == null)
            throw new IllegalArgumentException("Seed cannot be null.");
        if (seed.length != NanoConst.LEN_KEY_B)
            throw new IllegalArgumentException("Seed length is invalid.");
        if (index < 0 || index > MAX_SEED_INDEX)
            throw new IllegalArgumentException("Account index is out of bounds.");
        
        byte[] indexBytes = JNH.leftPadByteArray(JNH.longToBytes(index), 4, true);
        return JNH.blake2b(32, seed, indexBytes);
    }
    
}
