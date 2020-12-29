/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNH;
import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * This class is used to represent a quantity of Nano, for use as a transactional amount or account balance. Only
 * unsigned values are supported, so this class cannot be used to represent a negative amount.
 *
 * <p>To obtain an instance of this class, use one of the provided static {@code valueOf} methods, or one of the
 * constant field values. This class also supports built-in Gson serialization and deserialization as a raw value.</p>
 *
 * @author Karl Oczadly
 */
@JsonAdapter(NanoAmount.JsonAdapter.class)
public final class NanoAmount implements Comparable<NanoAmount> {
    
    /** A zero-value amount. */
    public static final NanoAmount ZERO = new NanoAmount(BigInteger.ZERO);
    
    /** The maximum possible balance value, equal to the amount created in the genesis block. */
    public static final NanoAmount MAX_VALUE = new NanoAmount(NanoConst.MAX_BALANCE);
    
    /**
     * A single Nano unit ({@code 1 Nano}).
     * <p>This value is based on the current {@link NanoUnit#BASE_UNIT} constant value.</p>
     */
    public static final NanoAmount ONE_NANO = NanoAmount.valueOf(BigInteger.ONE, NanoUnit.BASE_UNIT);
    
    /**
     * A single raw unit ({@code 1 raw}).
     * <p>This is the smallest representable quantity of Nano possible.</p>
     */
    public static final NanoAmount ONE_RAW = NanoAmount.valueOfRaw(BigInteger.ONE);
    
    
    private final BigInteger rawValue;
    
    /**
     * Creates a NanoAmount from a given {@code raw} value.
     *
     * @param rawValue the raw value
     */
    private NanoAmount(BigInteger rawValue) {
        if (rawValue == null)
            throw new IllegalArgumentException("Raw value cannot be null.");
        if (!JNH.isBalanceValid(rawValue))
            throw new IllegalArgumentException("Raw value is outside the possible range.");
        this.rawValue = rawValue;
    }
    
    
    /**
     * Returns the value of this amount in the {@code raw} unit.
     *
     * @return the value, in raw units
     */
    public BigInteger getAsRaw() {
        return rawValue;
    }
    
    /**
     * Returns the value of this amount in the standard base unit ({@link NanoUnit#BASE_UNIT}).
     *
     * @return the value, in the base unit
     */
    public BigDecimal getAsNano() {
        return getAs(NanoUnit.BASE_UNIT);
    }
    
    /**
     * Returns the value of this amount in the provided unit.
     *
     * @param unit the unit to convert this value to
     * @return the value, in the requested unit
     */
    public BigDecimal getAs(NanoUnit unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null.");
        return unit.convertFrom(NanoUnit.RAW, rawValue);
    }
    
    
    /**
     * Returns this amount as a friendly string, complete with the unit name.
     *
     * @return a friendly string of this amount
     * @see NanoUnit#toFriendlyString(BigInteger)
     */
    @Override
    public String toString() {
        return NanoUnit.toFriendlyString(rawValue);
    }
    
    /**
     * Returns this amount as an integer, in raw units.
     *
     * @return this value in raw, as a string
     */
    public String toRawString() {
        return rawValue.toString();
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NanoAmount)) return false;
        return Objects.equals(rawValue, ((NanoAmount)o).rawValue);
    }
    
    @Override
    public int hashCode() {
        return rawValue.hashCode();
    }
    
    @Override
    public int compareTo(NanoAmount o) {
        return rawValue.compareTo(o.rawValue);
    }
    
    
    /**
     * Returns a NanoAmount whose value is ({@code this + amount}).
     *
     * @param amount the amount to add
     * @return {@code this + amount}
     * @throws ArithmeticException if the resulting amount is above the maximum possible balance
     */
    public NanoAmount add(NanoAmount amount) {
        if (amount.equals(ZERO))
            return this;
        
        BigInteger newVal = rawValue.add(amount.rawValue);
        if (newVal.compareTo(MAX_VALUE.getAsRaw()) > 0)
            throw new ArithmeticException("Resulting NanoAmount is greater than the possible representable amount.");
        return new NanoAmount(newVal);
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this - amount}).
     *
     * @param amount the amount to subtract
     * @return {@code this - amount}
     * @throws ArithmeticException if {@code amount} is greater than this
     */
    public NanoAmount subtract(NanoAmount amount) {
        if (this.compareTo(amount) < 0)
            throw new ArithmeticException("Resulting NanoAmount is negative.");
        if (this.equals(amount))
            return ZERO;
        return new NanoAmount(rawValue.subtract(amount.rawValue));
    }
    
    /**
     * Returns the absolute difference between this and {@code other}.
     *
     * <p>This is equivalent to performing {@code abs(this - other)} on the integer value of both NanoAmounts.</p>
     *
     * @param other the other value to calculate the difference between
     * @return the absolute difference between this and {@code other}
     */
    public NanoAmount difference(NanoAmount other) {
        return valueOfRaw(rawValue.subtract(other.rawValue).abs());
    }
    
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(long val, NanoUnit unit) {
        return valueOf(BigInteger.valueOf(val), unit);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(BigInteger val, NanoUnit unit) {
        return new NanoAmount(NanoUnit.RAW.convertFromInt(unit, val));
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     * @throws ArithmeticException if the source {@code val} has too many decimal digits for the specified
     * {@code unit}
     */
    public static NanoAmount valueOf(BigDecimal val, NanoUnit unit) {
        return new NanoAmount(NanoUnit.RAW.convertFromInt(unit, val));
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     *
     * @param val  the numeric (integer or decimal) value (must be zero or positive)
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     *
     * @throws NumberFormatException if val is not a valid decimal number
     * @throws ArithmeticException if the source {@code val} has too many decimal digits for the specified
     * {@code unit}
     */
    public static NanoAmount valueOf(String val, NanoUnit unit) {
        return valueOf(new BigDecimal(val), unit);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given raw amount.
     *
     * @param raw the numeric value, in raw (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfRaw(BigInteger raw) {
        return new NanoAmount(raw);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given Nano amount.
     *
     * @param raw the numeric value, in raw (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     * @throws NumberFormatException if val is not a valid integer
     */
    public static NanoAmount valueOfRaw(String raw) {
        return valueOfRaw(new BigInteger(raw));
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given Nano amount.
     *
     * @param val the numeric value, in Nanos (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfNano(BigInteger val) {
        return valueOf(val, NanoUnit.BASE_UNIT);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given Nano amount.
     *
     * @param val the numeric value, in Nanos (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfNano(BigDecimal val) {
        return valueOf(val, NanoUnit.BASE_UNIT);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given Nano amount.
     *
     * @param val the numeric value, in Nanos (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfNano(long val) {
        return valueOf(val, NanoUnit.BASE_UNIT);
    }
    
    
    static class JsonAdapter implements JsonSerializer<NanoAmount>, JsonDeserializer<NanoAmount> {
        @Override
        public NanoAmount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return valueOfRaw(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(NanoAmount src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toRawString());
        }
    }
    
}
