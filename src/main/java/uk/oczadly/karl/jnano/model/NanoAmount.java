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
 * This class is used to represent a quantity of Nano, for use as a transactional amount or account balance.
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
    
    /** A single Nano unit ({@code 1 raw}). */
    public static final NanoAmount ONE_RAW = NanoAmount.valueOf(BigInteger.ONE);
    
    
    private final BigInteger rawValue;
    
    /**
     * Creates a NanoAmount from a given {@code raw} value.
     * @param rawValue the raw value, as a string
     * @deprecated {@link #valueOf(String)} is preferred for clarity
     */
    @Deprecated
    public NanoAmount(String rawValue) {
        this(new BigInteger(rawValue));
    }
    
    /**
     * Creates a NanoAmount from a given value and unit.
     * @param value the value
     * @param unit  the unit of the value
     * @deprecated {@link #valueOf(BigInteger, NanoUnit)} is preferred for clarity
     */
    @Deprecated
    public NanoAmount(BigInteger value, NanoUnit unit) {
        this(NanoUnit.RAW.convertFromInt(unit, value));
    }
    
    /**
     * Creates a NanoAmount from a given value and unit.
     * @param value the value
     * @param unit  the unit of the value
     * @deprecated {@link #valueOf(BigDecimal, NanoUnit)} is preferred for clarity
     */
    @Deprecated
    public NanoAmount(BigDecimal value, NanoUnit unit) {
        this(NanoUnit.RAW.convertFromInt(unit, value));
    }
    
    /**
     * Creates a NanoAmount from a given {@code raw} value.
     * @param rawValue the raw value
     */
    public NanoAmount(BigInteger rawValue) {
        if (rawValue == null)
            throw new IllegalArgumentException("Raw value cannot be null.");
        if (!JNH.isBalanceValid(rawValue))
            throw new IllegalArgumentException("Raw value is out of the possible range.");
        this.rawValue = rawValue;
    }
    
    
    /**
     * Returns the value of this amount in the {@code raw} unit.
     * @return the value, in raw units
     */
    public BigInteger getAsRaw() {
        return rawValue;
    }
    
    /**
     * Returns the value of this amount in the standard base unit ({@link NanoUnit#BASE_UNIT}).
     * @return the value, in the base unit
     */
    public BigDecimal getAsNano() {
        return getAs(NanoUnit.BASE_UNIT);
    }
    
    /**
     * Returns the value of this amount in the provided unit.
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
     * @return a friendly string of this amount
     * @see NanoUnit#toFriendlyString(BigInteger)
     */
    @Override
    public String toString() {
        return NanoUnit.toFriendlyString(rawValue);
    }
    
    /**
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
     * @param amount the amount to add
     * @return {@code this + amount}
     */
    public NanoAmount add(NanoAmount amount) {
        return new NanoAmount(rawValue.add(amount.rawValue));
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this - amount}).
     * @param amount the amount to subtract
     * @return {@code this - amount}
     */
    public NanoAmount subtract(NanoAmount amount) {
        return new NanoAmount(rawValue.subtract(amount.rawValue));
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this * amount}).
     * @param amount the amount to multiply by
     * @return {@code this * amount}
     */
    public NanoAmount multiply(NanoAmount amount) {
        return new NanoAmount(rawValue.multiply(amount.rawValue));
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this * val}).
     * @param val the amount to multiply by
     * @return {@code this * val}
     */
    public NanoAmount multiply(int val) {
        return new NanoAmount(rawValue.multiply(BigInteger.valueOf(val)));
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this / amount}).
     * <p>Note that this method will round down if this raw value is odd.</p>
     *
     * @param amount the amount to divide by
     * @return {@code this / amount}
     * @throws ArithmeticException if {@code amount} is zero.
     */
    public NanoAmount divide(NanoAmount amount) {
        return new NanoAmount(rawValue.divide(amount.rawValue));
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this / val}).
     * <p>Note that this method will round down if this raw value is odd.</p>
     *
     * @param val the amount to divide by
     * @return {@code this / val}
     * @throws ArithmeticException if {@code val} is zero.
     */
    public NanoAmount divide(int val) {
        return new NanoAmount(rawValue.divide(BigInteger.valueOf(val)));
    }
    
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     * @param val  the numeric value
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(long val, NanoUnit unit) {
        return valueOf(BigInteger.valueOf(val), unit);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     * @param val  the numeric value
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(BigInteger val, NanoUnit unit) {
        return new NanoAmount(NanoUnit.RAW.convertFromInt(unit, val));
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given amount and unit.
     * @param val  the numeric value
     * @param unit the denominaton of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(BigDecimal val, NanoUnit unit) {
        return new NanoAmount(NanoUnit.RAW.convertFromInt(unit, val));
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given raw amount.
     * @param raw the numeric value, in raw
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(BigInteger raw) {
        return new NanoAmount(raw);
    }
    
    /**
     * Returns a {@link NanoAmount} instance that represents the given raw amount.
     * @param raw the numeric value, in raw
     * @return a {@link NanoAmount} instance representing the given value
     * @throws NumberFormatException if the string is not a valid integer value
     */
    public static NanoAmount valueOf(String raw) {
        return new NanoAmount(new BigInteger(raw));
    }
    
    
    static class JsonAdapter implements JsonSerializer<NanoAmount>, JsonDeserializer<NanoAmount> {
        @Override
        public NanoAmount deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return NanoAmount.valueOf(json.getAsString());
        }
        
        @Override
        public JsonElement serialize(NanoAmount src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.rawValue.toString());
        }
    }
    
}
