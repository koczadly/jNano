package uk.oczadly.karl.jnano;

import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.model.NanoAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestConstants {
    
    private static final Random RANDOM = new Random();
    private static final long RAND_SEED = RANDOM.nextLong();
    private static final Map<Integer, Map<Integer, String>> HEX_CACHE = new HashMap<>(); // index -> len -> val
    private static final Map<Integer, NanoAccount> ACCOUNT_CACHE = new HashMap<>(); // index -> val
    
    
    public static String hex(int len) {
        return hex(len, RANDOM.nextInt());
    }
    
    public static String hex(int len, int index) {
        return HEX_CACHE
                .computeIfAbsent(index, k -> new HashMap<>())
                .computeIfAbsent(len, k -> {
                    StringBuilder sb = new StringBuilder(len);
                    for (int i=0; i<len; i++)
                        sb.append(JNH.HEX_CHARS_UC[RANDOM.nextInt(16)]);
                    return sb.toString();
                });
    }
    
    
    public static NanoAccount account() {
        return account(RANDOM.nextInt());
    }
    
    public static NanoAccount account(int index) {
        return ACCOUNT_CACHE.computeIfAbsent(index, e -> {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            return new NanoAccount(bytes);
        });
    }

}
