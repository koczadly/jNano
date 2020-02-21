package uk.oczadly.karl.jnano.util;

import java.security.SecureRandom;

public class WalletUtil {
    
    private static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    
    public static String generateRandomSeed() {
        return generateRandomSeed(new SecureRandom());
    }
    
    public static String generateRandomSeed(SecureRandom random) {
        StringBuilder seed = new StringBuilder(64);
        for(int i=0; i<64; i++)
            seed.append(HEX_CHARS_UC[random.nextInt(16)]);
        return seed.toString();
    }
    
}
