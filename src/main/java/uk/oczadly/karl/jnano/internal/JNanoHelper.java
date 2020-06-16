package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.oczadly.karl.jnano.internal.gsonadapters.ArrayTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.BooleanTypeDeserializer;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;

import java.math.BigInteger;

public class JNanoHelper {
    
    public static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    public static final BaseEncoder ENCODER_HEX = new BaseEncoder(HEX_CHARS_UC);
    public static final BaseEncoder ENCODER_NANO_B32 = new BaseEncoder("13456789abcdefghijkmnopqrstuwxyz");
    
    public static final String EMPTY_HEX_64 = "0000000000000000000000000000000000000000000000000000000000000000";
    
    private static final BigInteger MAX_BALANCE_VAL = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
    
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())       // Empty array hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer())  // Boolean deserializer
            .create();
    
    
    /**
     * Left-pads a byte array with zeroes.
     */
    public static byte[] padByteArray(byte[] bytes, int len) {
        if (bytes.length > len)
            throw new IllegalArgumentException("Provided byte array was longer than the max padding length.");
        
        byte[] newArr = new byte[len];
        System.arraycopy(bytes, 0, newArr, len - bytes.length, bytes.length);
        return newArr;
    }
    
    /**
     * @return true if the string is null or matches hex format
     */
    public static boolean isValidHex(String str, int len) {
        if (str == null) return true;
        if (str.length() != len) return false;
        return str.matches("[0-9A-Fa-f]+");
    }
    
    /**
     * @return true if the BigInt is null or is within the correct range
     */
    public static boolean isBalanceValid(BigInteger raw) {
        if (raw == null) return true;
        return raw.compareTo(BigInteger.ZERO) >= 0 && raw.compareTo(MAX_BALANCE_VAL) <= 0;
    }
    
}
