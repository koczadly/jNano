package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.JNanoHelper;

import java.security.SecureRandom;

public final class WalletUtil {
    private WalletUtil() {}
    
    
    /**
     * Generates and returns a new randomly created seed.
     *
     * @return a 64-character hexadecimal string to be used as a wallet seed or private key
     */
    public static String generateRandomSeed() {
        return generateRandomSeed(new SecureRandom());
    }
    
    /**
     * Generates and returns a new randomly created seed from a given {@link SecureRandom} instance.
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
    
}
