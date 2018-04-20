package uk.oczadly.karl.jnano.util;

import java.util.Random;

public class WalletUtil {
    
    private static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    private static final char[] HEX_CHARS_LC = "0123456789abcdef".toCharArray();
    
    
    public static String generateNewSeed() {
        return generateNewSeed(new Random());
    }
    
    public static String generateNewSeed(Random random) {
        StringBuilder seed = new StringBuilder();
        for(int i=0; i<64; i++) seed.append(HEX_CHARS_UC[random.nextInt(16)]);
        return seed.toString();
    }
    
}
