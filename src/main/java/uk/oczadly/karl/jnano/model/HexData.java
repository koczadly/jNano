/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNH;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * This class represents a single hexadecimal value.
 *
 * <p>The hexadecimal value can be retrieved using {@link #toHexString()}, or can be converted to a byte array
 * through the {@link #toByteArray()} method.</p>
 *
 * <p>When serialized to a JSON object, it will be converted to a hexadecimal string as returned by the
 * {@link #toHexString()} method. When deserializing from a JSON field, it will assume the value to be a hexadecimal
 * string and call the {@link #HexData(String)} constructor.</p>
 *
 * @author Karl Oczadly
 */
@JsonAdapter(HexData.JsonAdapter.class)
public class HexData {
    
    private final String valHex;
    private final int byteLength;
    private volatile byte[] valBytes;
    
    
    /**
     * Constructs a HexData object with a given hex value.
     *
     * @param hex the hexadecimal string (must be a multiple of 2)
     */
    public HexData(String hex) {
        this.valHex = sanitizeHex(hex);
        this.byteLength = valHex.length() / 2;
    }
    
    /**
     * Constructs a HexData object with a given hex value and specified length. If the given hex string is shorter
     * than the length, then it will be prepended with zeroes.
     *
     * @param hex    the hexadecimal string (must be a multiple of 2)
     * @param length the length of the data in bytes
     */
    public HexData(String hex, int length) {
        if (length < 0)
            throw new IllegalArgumentException("'length' must be zero or greater.");
        hex = sanitizeHex(hex);
        if (hex.length() > (length * 2))
            throw new IllegalArgumentException("'hex' string is longer than 'length'.");
        this.valHex = JNH.leftPadString(hex, length * 2, '0');
        this.byteLength = length;
    }
    
    /**
     * Constructs a HexData object with a given byte array.
     *
     * @param bytes the array of bytes to construct from
     */
    public HexData(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("'bytes' array cannot be null.");
        this.valBytes = Arrays.copyOf(bytes, bytes.length);
        this.byteLength = valBytes.length;
        this.valHex = JNH.ENC_16.encode(valBytes);
    }
    
    /**
     * Constructs a HexData object with a given byte array. If the given byte array is shorter than the length,
     * then it will be prepended with zeroes.
     *
     * @param bytes the array of bytes to construct from
     * @param length the length of the data in bytes
     */
    public HexData(byte[] bytes, int length) {
        if (bytes == null)
            throw new IllegalArgumentException("'bytes' array cannot be null.");
        if (length < 0)
            throw new IllegalArgumentException("'length' must be zero or greater.");
        if (bytes.length > length)
            throw new IllegalArgumentException("'bytes' array is longer than 'length'.");
        this.valBytes = JNH.leftPadByteArray(Arrays.copyOf(bytes, bytes.length), length, false);
        this.byteLength = length;
        this.valHex = JNH.ENC_16.encode(valBytes);
    }
    
    
    /**
     * Returns the value of this object, encoded as an uppercase hexadecimal string.
     *
     * @return this object, as a hex string
     */
    public final String toHexString() {
        return valHex;
    }
    
    /**
     * Returns the value of this object, encoded as a {@code byte} array.
     *
     * @return this object, as a byte array
     */
    public final byte[] toByteArray() {
        byte[] valBytes = _toByteArray();
        return Arrays.copyOf(valBytes, valBytes.length);
    }
    
    private byte[] _toByteArray() {
        if (valBytes == null) {
            synchronized (this) {
                if (valBytes == null) {
                    valBytes = JNH.ENC_16.decode(toHexString());
                }
            }
        }
        return valBytes;
    }
    
    /**
     * Returns the length of this data as a number of bytes, including leading zeroes.
     *
     * @return the length in bytes
     */
    public final int length() {
        return byteLength;
    }
    
    
    @Override
    public String toString() {
        return toHexString();
    }
    
    
    /**
     * Returns whether this hexadecimal object is equal to the given hexadecimal value.
     *
     * <p>This method will ignore all leading zeroes for this object and the given argument. The {@code hex} argument
     * is case-insensitive, and will simply return false if it is not a valid hexadecimal string.</p>
     *
     * @param hex the hexadecimal value to compare with
     * @return true if the string matches
     */
    public final boolean equalsValue(String hex) {
        if (hex == null) throw new IllegalArgumentException("Hex value cannot be null.");
        String thisHex = JNH.removeLeadingZeroes(toHexString());
        String thatHex = JNH.removeLeadingZeroes(hex);
        return thisHex.equalsIgnoreCase(thatHex);
    }
    
    /**
     * Returns whether this hexadecimal object is equal to the given hexadecimal value.
     *
     * <p>This method will ignore all leading zeroes for this object and the given argument.</p>
     *
     * @param hex the hexadecimal value to compare with
     * @return true if the string matches
     */
    public final boolean equalsValue(HexData hex) {
        if (hex == null) throw new IllegalArgumentException("Hex value cannot be null.");
        return equalsValue(hex.toHexString());
    }
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HexData)) return false;
        return Arrays.equals(_toByteArray(), ((HexData)o)._toByteArray());
    }
    
    @Override
    public final int hashCode() {
        return Arrays.hashCode(_toByteArray());
    }
    
    
    private static String sanitizeHex(String hex) {
        if (hex == null)
            throw new IllegalArgumentException("Hex value cannot be null.");
        hex = hex.startsWith("0x") ? hex.substring(2) : hex; // Preprocess hex string
        if (hex.isEmpty())
            throw new IllegalArgumentException("Hex string cannot be empty.");
        if (hex.length() % 2 != 0)
            hex = "0" + hex;
        if (!JNH.isValidHex(hex, -1))
            throw new IllegalArgumentException("Hex string contains invalid characters.");
        return hex.toUpperCase();
    }
    
    
    static class JsonAdapter implements JsonSerializer<HexData>, JsonDeserializer<HexData> {
        @Override
        public HexData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return new HexData(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(HexData src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toHexString());
        }
    }

}
