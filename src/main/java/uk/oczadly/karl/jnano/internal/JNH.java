/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.rfksystems.blake2b.Blake2b;
import uk.oczadly.karl.jnano.internal.utils.Functions;
import uk.oczadly.karl.jnano.model.HexData;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * JNano helper class.
 */
public class JNH {
    
    private static final Pattern HEX_PATTERN = Pattern.compile("[0-9A-Fa-f]+");
    
    
    /** For java 8 compatibility */
    public static <T> Set<T> ofSet(T... elements) {
        Set<T> set = new HashSet<>(elements.length);
        for (T element : elements)
            set.add(element);
        return Collections.unmodifiableSet(set);
    }
    
    /** For java 8 compatibility */
    public static <T> List<T> ofList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
    
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
        if (len > 0 && str.length() != len) return false;
        return HEX_PATTERN.matcher(str).matches();
    }
    
    /**
     * @return true if data is null, or data.length() == len
     */
    public static boolean isValidLength(HexData data, int len) {
        return data == null || data.length() == len;
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
    
    /**
     * @param data the data
     * @param allowNull if true, the method will return true for null data arg
     * @return true if the string is null, empty or filled with zeroes
     */
    public static boolean isZero(HexData data, boolean allowNull) {
        if (data == null && !allowNull) return false;
        if (data != null) {
            for (byte b : data.toByteArray()) {
                if (b != (byte)0) return false;
            }
        }
        return true;
    }
    
    public static String removeLeadingZeroes(String str) {
        StringBuilder sb = new StringBuilder();
        boolean skippedZeroes = false;
        for (char c : str.toCharArray()) {
            if (c != '0') skippedZeroes = true;
            if (skippedZeroes) sb.append(c);
        }
        return sb.toString();
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
    
    public static byte[] intToBytes(int val) {
        return new byte[] {
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
        return unchecked(supplier, RuntimeException::new);
    }
    
    public static <T> T unchecked(Callable<T> supplier, Function<Exception, ? extends RuntimeException> rethrow) {
        try {
            return supplier.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw rethrow.apply(e);
        }
    }
    
    public static <T, U> T nullable(U obj, Function<U, T> func) {
        return obj != null ? func.apply(obj) : null;
    }
    
    public static <T> T getJson(JsonObject json, String key, Function<String, T> func) {
        if (json == null) return null;
        JsonElement element = json.get(key);
        return element != null ? func.apply(element.getAsString()) : null;
    }
    
    public static String getJson(JsonObject json, String key) {
        if (json == null) return null;
        JsonElement element = json.get(key);
        return element != null ? element.getAsString() : null;
    }
    
    public static JsonObject parseJson(String json) {
        JsonElement element = JsonParser.parseString(json);
        if (element.isJsonObject()) return element.getAsJsonObject();
        throw new JsonParseException("Not a JSON object.");
    }
    
    public static <T, U, E extends Throwable> U tryRethrow(T obj, Functions.UncheckedFunction<T, U> func,
                                                           Function<Exception, E> exceptionSupplier) throws E {
        try {
            return func.apply(obj);
        } catch (Exception e) {
            throw exceptionSupplier.apply(e);
        }
    }
    
    public static <T, E extends Throwable> T tryRethrow(Callable<T> func,
                                                        Function<Exception, E> exceptionSupplier) throws E {
        try {
            return func.call();
        } catch (Exception e) {
            throw exceptionSupplier.apply(e);
        }
    }
    
    public static <C, R> R instanceOf(Object obj, Class<C> clazz, R def, Function<C, R> sup) {
        if (obj == null) return def;
        return clazz.isInstance(obj) ? sup.apply(clazz.cast(obj)) : def;
    }
    
    public static <T extends Comparable<T>> T max(T...objects) {
        T max = null;
        for (T obj : objects) {
            if (max == null || max.compareTo(obj) < 0)
                max = obj;
        }
        if (max == null)
            throw new IllegalArgumentException("No non-null values were provided.");
        return max;
    }
    
    public static ThreadFactory threadFactory(String namePrefix, boolean daemon) {
        return threadFactory(namePrefix, daemon, Thread.NORM_PRIORITY);
    }
    
    public static ThreadFactory threadFactory(String namePrefix, boolean daemon, int priority) {
        return new ThreadFactory() {
            final AtomicInteger id = new AtomicInteger();
            
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, namePrefix + "-" + id.getAndIncrement());
                thread.setDaemon(daemon);
                thread.setPriority(priority);
                return thread;
            }
        };
    }
    
}
