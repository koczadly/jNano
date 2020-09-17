/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.gsonadapters.ArrayTypeAdapterFactoryFix;
import uk.oczadly.karl.jnano.internal.gsonadapters.BigIntSerializer;
import uk.oczadly.karl.jnano.internal.gsonadapters.BooleanTypeDeserializer;
import uk.oczadly.karl.jnano.internal.gsonadapters.InstantAdapter;
import uk.oczadly.karl.jnano.internal.utils.BaseEncoder;
import uk.oczadly.karl.jnano.util.NanoConstants;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * JNano Helper class.
 */
public class JNH {
    
    public static final char[] HEX_CHARS_UC = "0123456789ABCDEF".toCharArray();
    
    public static final BaseEncoder ENC_16 = new BaseEncoder(HEX_CHARS_UC);
    public static final BaseEncoder ENC_32 = new BaseEncoder("13456789abcdefghijkmnopqrstuwxyz");
    
    public static final String ZEROES_16 = repeatChar('0', 16);
    public static final String ZEROES_64 = repeatChar('0', 64);
    public static final String ZEROES_128 = repeatChar('0', 128);
    
    public static final BigInteger BIGINT_MAX_128 = new BigInteger(1, filledByteArray(16, (byte)0xFF));
    public static final BigInteger BIGINT_MAX_256 = new BigInteger(1, filledByteArray(32, (byte)0xFF));
    
    public static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapterFactory(new ArrayTypeAdapterFactoryFix())      // Empty array hotfix
            .registerTypeAdapter(boolean.class, new BooleanTypeDeserializer()) // Boolean deserializer
            .registerTypeAdapter(Boolean.class, new BooleanTypeDeserializer()) // Boolean deserializer
            .registerTypeAdapter(BigInteger.class, new BigIntSerializer())     // BigInt serializer (string)
            .registerTypeAdapter(Instant.class, new InstantAdapter())          // Instant adapter (epoch millis)
            .create();
    
    
    /**
     * Returns a new byte array filled with values.
     */
    public static byte[] filledByteArray(int lenBytes, byte val) {
        byte[] bytes = new byte[lenBytes];
        Arrays.fill(bytes, val);
        return bytes;
    }
    
    /**
     * Left-pads a byte array with zeroes. Will return the same array if length matches, or create a new one if it
     * needs to be padded.
     */
    public static byte[] leftPadByteArray(byte[] arr, int len, boolean trim) {
        if (arr.length == len || (!trim && arr.length > len))
            return arr;
        
        byte[] newArr = new byte[len];
        if (arr.length > len) { // Trim
            System.arraycopy(arr, arr.length - len, newArr, 0, len);
        } else { // Pad
            System.arraycopy(arr, 0, newArr, len - arr.length, arr.length);
        }
        return newArr;
    }
    
    /**
     * Reverses a given array (will mutate the provided array!)
     */
    public static byte[] reverseArray(byte[] arr) {
        if (arr == null) return null;
        for (int i = 0; i < (arr.length / 2); i++) {
            byte tmp = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }
    
    /**
     * Left-pads a string with the given character up to the minimum length.
     */
    public static String leftPadString(String str, int minLen, char padChar) {
        if (str.length() >= minLen) return str;
        
        StringBuilder sb = new StringBuilder(minLen);
        for (int i=0; i<(minLen-str.length()); i++)
            sb.append(padChar);
        sb.append(str);
        return sb.toString();
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
        return raw.compareTo(BigInteger.ZERO) >= 0 && raw.compareTo(NanoConstants.MAX_BALANCE_RAW) <= 0;
    }
    
    /**
     * @param str the string
     * @param allowNull if true, the method will return true for null strings
     * @return true if the string is null, empty or filled with zeroes
     */
    public static boolean isZero(String str, boolean allowNull) {
        if (str == null && !allowNull) return false;
        if (str != null) {
            for (char c : str.toCharArray()) {
                if (c != '0') return false;
            }
        }
        return true;
    }
    
    public static String repeatChar(char c, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i=0; i<length; i++)
            sb.append(c);
        return sb.toString();
    }
    
    public static byte[] blake2b(int outputLen, byte[]...data) {
        Blake2b digest = new Blake2b(null, outputLen, null, null);
        for (byte[] array : data)
            digest.update(array, 0, array.length);
        byte[] out = new byte[outputLen];
        digest.digest(out, 0);
        return out;
    }
    
    public static byte[] longToBytes(long val) {
        return new byte[] {
                (byte)((val >> 56) & 0xFF),
                (byte)((val >> 48) & 0xFF),
                (byte)((val >> 40) & 0xFF),
                (byte)((val >> 32) & 0xFF),
                (byte)((val >> 24) & 0xFF),
                (byte)((val >> 16) & 0xFF),
                (byte)((val >> 8 ) & 0xFF),
                (byte)(val & 0xFF)
        };
    }
        
    public static long bytesToLong(byte[] bytes) {
        if (bytes.length > 8)
            throw new IllegalArgumentException("Byte array must have 8 (or fewer) elements.");
        
        bytes = leftPadByteArray(bytes, 8, false);
        long val = bytes[0] & 0xFF;
        val = ((val << 8) | (bytes[1] & 0xFF));
        val = ((val << 8) | (bytes[2] & 0xFF));
        val = ((val << 8) | (bytes[3] & 0xFF));
        val = ((val << 8) | (bytes[4] & 0xFF));
        val = ((val << 8) | (bytes[5] & 0xFF));
        val = ((val << 8) | (bytes[6] & 0xFF));
        val = ((val << 8) | (bytes[7] & 0xFF));
        return val;
    }
    
    public static <T> T unchecked(Callable<T> supplier) {
        try {
            return supplier.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T, U> T nullable(U obj, Function<U, T> func) {
        return obj != null ? func.apply(obj) : null;
    }
    
    public static <C, R> R instanceOf(Object obj, Class<C> clazz, R def, Function<C, R> sup) {
        if (obj == null) return def;
        return clazz.isInstance(obj) ? sup.apply(clazz.cast(obj)) : def;
    }
    
    public static <T extends Comparable<T>> T max(T obj1, T obj2) {
        return obj1.compareTo(obj2) >= 0 ? obj1 : obj2;
    }
    
}
