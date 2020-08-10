package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNanoHelper;

import java.security.SecureRandom;

public final class WalletUtil {
    private WalletUtil() {}
    
    public static final long MAX_SEED_INDEX = 4294967295L;
    
    
    /**
     * Generates and returns a new randomly created seed (or private key).
     *
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static String generateRandomSeed() {
        return generateRandomSeed(new SecureRandom());
    }
    
    /**
     * Generates and returns a new randomly created seed (or private key) from a given {@link SecureRandom} instance.
     *
     * @param random the random generator used to create the seed
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static String generateRandomSeed(SecureRandom random) {
        StringBuilder seed = new StringBuilder(64);
        for (int i=0; i<64; i++)
            seed.append(JNanoHelper.HEX_CHARS_UC[random.nextInt(16)]);
        return seed.toString();
    }
    
    
    /**
     * Derives a private key from the given seed, using the zeroth index.
     * @param seed  the seed, represented as a 64-character hex string
     * @return a 64-character hex string representing the private key
     */
    public static String deriveKeyFromSeed(String seed) {
        return deriveKeyFromSeed(seed, 0L);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 64-character hex string
     * @param index the index of the account
     * @return a 64-character hex string representing the private key
     */
    public static String deriveKeyFromSeed(String seed, long index) {
        if (seed == null)
            throw new IllegalArgumentException("Seed cannot be null.");
        if (!JNanoHelper.isValidHex(seed, 64))
            throw new IllegalArgumentException("Seed must be a 64-character hex string.");
        
        byte[] seedBytes = deriveKeyFromSeed(JNanoHelper.ENCODER_HEX.decode(seed), index);
        return JNanoHelper.ENCODER_HEX.encode(seedBytes);
    }
    
    /**
     * Derives a private key from the given seed and index.
     * @param seed  the seed, represented as a 32-element byte array
     * @param index the index of the account
     * @return a 32-element byte array representing the private key
     */
    public static byte[] deriveKeyFromSeed(byte[] seed, long index) {
        if (seed == null)
            throw new IllegalArgumentException("Seed cannot be null.");
        if (seed.length != 32)
            throw new IllegalArgumentException("Seed array must contain 32 bytes.");
        if (index < 0 || index > MAX_SEED_INDEX)
            throw new IllegalArgumentException("Seed index is out of bounds.");
        
        byte[] indexBytes = JNanoHelper.leftPadByteArray(JNanoHelper.longToBytes(index), 4, true);
        return JNanoHelper.blake2b(32, seed, indexBytes);
    }
    
}
