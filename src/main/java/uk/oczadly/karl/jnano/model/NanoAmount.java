/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import uk.oczadly.karl.jnano.internal.JNC;
import uk.oczadly.karl.jnano.internal.utils.UnitHelper;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * An immutable class which represents a quantity of Nano.
 *
 * <p>This class can be used as a transactional amount or account balance. Only unsigned values are supported, so this
 * class cannot be used to represent a negative amount.</p>
 *
 * <p>To create an instance of this class, use one of the provided static {@code valueOf} methods, or one of the
 * constant field values. This class also supports built-in Gson serialization and deserialization as a raw value.</p>
 *
 * <p>Custom units can be specified by implementing the {@link Denomination} interface, or using the constructor of
 * {@link DenominationImpl}. For instance, to create the unit for Banano you could define the following constant:</p>
 * <pre>{@code // Static constant representing the standard Banano unit
 * public static final NanoAmount.Denomination UNIT_BAN = new NanoAmount.DenominationImpl("BAN", 29);
 *
 * // Elsewhere in your application's method...
 * NanoAmount amount = NanoAmount.valueOf("2.79", UNIT_BAN);
 * System.out.println("Raw: " + amount.getAsRaw());      // "Raw: 279000000000000000000000000000"
 * System.out.println("Ban: " + amount.getAs(UNIT_BAN)); // "Ban: 2.79"
 * }</pre>
 *
 * @author Karl Oczadly
 */
@JsonAdapter(NanoAmount.JsonAdapter.class)
public final class NanoAmount implements Comparable<NanoAmount> {
    
    private static final BigInteger MAX_VAL_RAW = JNC.BIGINT_MAX_128;
    private static final Denomination BASE_UNIT = NanoUnit.BASE_UNIT;
    private static final BigInteger TOSTRING_THRESH = BigInteger.TEN.pow(BASE_UNIT.getExponent() - 6);
    
    /** A zero-value amount. */
    public static final NanoAmount ZERO = new NanoAmount(BigInteger.ZERO);
    
    /** The maximum possible balance value, equal to the amount created in the genesis block. */
    public static final NanoAmount MAX_VALUE = new NanoAmount(MAX_VAL_RAW);
    
    /**
     * A constant value representing a single {@link NanoUnit#RAW raw} unit ({@code 1 raw}).
     * <p>This is the smallest representable quantity of Nano possible.</p>
     */
    public static final NanoAmount ONE_RAW = new NanoAmount(BigInteger.ONE);
    
    /**
     * A constant value representing a single {@link NanoUnit#BASE_UNIT Nano} unit ({@code 1 Nano}).
     * <p><b>Warning:</b> This is based on the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     */
    public static final NanoAmount ONE_NANO = NanoAmount.valueOfNano(BigDecimal.ONE);
    
    
    private final BigInteger rawValue;
    private volatile BigDecimal cachedBaseUnitValue; // performance optimization
    
    private NanoAmount(BigInteger rawValue) {
        if (rawValue == null)
            throw new IllegalArgumentException("Raw value cannot be null.");
        if (rawValue.compareTo(BigInteger.ZERO) < 0)
            throw new IllegalArgumentException("NanoAmount cannot represent negative amounts.");
        if (rawValue.compareTo(MAX_VAL_RAW) > 0)
            throw new IllegalArgumentException("NanoAmount value is too large.");
        this.rawValue = rawValue;
    }
    
    private NanoAmount(BigInteger rawValue, BigDecimal cachedBaseUnitValue) {
        this(rawValue);
        this.cachedBaseUnitValue = cachedBaseUnitValue.stripTrailingZeros();
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
     * Returns the value of this amount in the standard base unit.
     *
     * <p><b>Warning:</b> This method uses the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     *
     * @return the value, in the base unit
     */
    public BigDecimal getAsNano() {
        return getAs(BASE_UNIT);
    }
    
    /**
     * Returns the value of this amount in the provided unit.
     *
     * @param unit the unit to convert this value to
     * @return the value, in the requested unit
     */
    public BigDecimal getAs(Denomination unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null.");
        if (unit == BASE_UNIT) {
            if (cachedBaseUnitValue != null)
                return cachedBaseUnitValue; // Return from cache
        }
        BigDecimal amount = UnitHelper.convert(new BigDecimal(rawValue), 0, unit.getExponent());
        if (unit == BASE_UNIT)
            cachedBaseUnitValue = amount; // Store in cache
        return amount;
    }
    
    
    /**
     * Returns this amount as a friendly string, complete with the unit name. The value will be formatted either in
     * the {@link NanoUnit#BASE_UNIT base unit}, or {@link NanoUnit#RAW raw} depending on which is more suitable.
     *
     * @return a friendly string of this amount
     */
    @Override
    public String toString() {
        boolean useBase = rawValue.equals(BigInteger.ZERO) || rawValue.compareTo(TOSTRING_THRESH) >= 0;
        return toString(useBase ? BASE_UNIT : NanoUnit.RAW);
    }
    
    /**
     * Returns this amount as a friendly string, complete with the unit name.
     *
     * @param unit the unit to display the amount in
     * @return a friendly string of this amount
     */
    public String toString(Denomination unit) {
        BigDecimal amount = getAs(unit);
        return UnitHelper.toFriendlyString(amount, unit);
    }
    
    /**
     * Returns this amount as an integer, in raw units. Example string: "{@code 4239000000000000000000000000000}".
     *
     * @return this value in raw, as a string
     */
    public String toRawString() {
        return rawValue.toString();
    }
    
    /**
     * Returns this amount as a decimal number, in the standard base unit. Example string: "{@code 4.239}".
     *
     * <p><b>Warning:</b> This method uses the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     *
     * @return this value in the base unit, as a string
     */
    public String toNanoString() {
        return getAsNano().toPlainString();
    }
    
    
    /**
     * Compares this NanoAmount with the specified Object for equality.
     *
     * @param o the object this
     * @return {@code true} if the specified Object is a NanoAmount whose value is numerically equal to this NanoAmount.
     */
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
        if (amount.equals(ZERO)) return this;
        BigInteger newVal = rawValue.add(amount.rawValue);
        if (newVal.compareTo(MAX_VAL_RAW) > 0)
            throw new ArithmeticException("Result amount is greater than the largest representable amount.");
        return valueOfRaw(newVal);
    }
    
    /**
     * Returns a NanoAmount whose value is ({@code this - amount}).
     *
     * @param amount the amount to subtract
     * @return {@code this - amount}
     * @throws ArithmeticException if {@code amount} is greater than this
     */
    public NanoAmount subtract(NanoAmount amount) {
        if (amount.equals(ZERO)) return this;
        if (this.equals(amount)) return ZERO;
        if (this.compareTo(amount) < 0) throw new ArithmeticException("Result amount is negative.");
        return valueOfRaw(rawValue.subtract(amount.rawValue));
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
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(long val, Denomination unit) {
        if (unit.getExponent() == 0)
            return valueOfRaw(val); // Skip conversion if source unit is raw
        return valueOf(BigDecimal.valueOf(val), unit);
    }
    
    /**
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOf(BigInteger val, Denomination unit) {
        if (unit.getExponent() == 0)
            return valueOfRaw(val); // Skip conversion if source unit is raw
        return valueOf(new BigDecimal(val), unit);
    }
    
    /**
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified
     *                                  {@code unit}
     */
    public static NanoAmount valueOf(BigDecimal val, Denomination unit) {
        if (unit == BASE_UNIT) {
            return new NanoAmount(UnitHelper.convertToRaw(val, unit.getExponent()), val);
        } else {
            return new NanoAmount(UnitHelper.convertToRaw(val, unit.getExponent()));
        }
    }
    
    /**
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric (integer or decimal) value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     *
     * @throws NumberFormatException if val is not a valid decimal number
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified
     *                                  {@code unit}
     */
    public static NanoAmount valueOf(String val, Denomination unit) {
        return valueOf(new BigDecimal(val), unit);
    }
    
    /**
     * Returns a NanoAmount that represents the given {@link NanoUnit#RAW raw} amount.
     *
     * @param raw the numeric value, in raw (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfRaw(BigInteger raw) {
        if (raw.equals(BigInteger.ZERO)) return ZERO;
        if (raw.equals(BigInteger.ONE)) return ONE_RAW;
        return new NanoAmount(raw);
    }
    
    /**
     * Returns a NanoAmount that represents the given {@link NanoUnit#RAW raw} amount.
     *
     * @param raw the numeric value, in raw (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     * @throws NumberFormatException if val is not a valid integer
     */
    public static NanoAmount valueOfRaw(String raw) {
        return valueOfRaw(new BigInteger(raw));
    }
    
    /**
     * Returns a NanoAmount that represents the given {@link NanoUnit#RAW raw} amount.
     *
     * @param raw the numeric value, in raw (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfRaw(long raw) {
        return valueOfRaw(BigInteger.valueOf(raw));
    }
    
    /**
     * Returns a NanoAmount that represents <code>10<sup>exponent</sup></code> {@link NanoUnit#RAW raw}. An
     * {@code exponent} value of {@code 5} means one followed by five zeroes ({@code 100000 raw}). Useful for
     * constructing threshold values.
     * @param exponent the exponent, with a base of {@code 10}
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfRawExponent(int exponent) {
        return valueOfRaw(BigInteger.TEN.pow(exponent));
    }
    
    /**
     * Returns a NanoAmount instance representing the given Nano amount.
     *
     * <p><b>Warning:</b> This method uses the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     *
     * @param val the integer or decimal value, in Nano (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     *
     * @throws NumberFormatException if val is not a valid integer or decimal number
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified
     *                                  {@code unit}
     */
    public static NanoAmount valueOfNano(String val) {
        return valueOf(val, BASE_UNIT);
    }
    
    /**
     * Returns a NanoAmount instance representing the given Nano amount.
     *
     * <p><b>Warning:</b> This method uses the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     *
     * @param val the numeric value, in Nano (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified
     *                                  {@code unit}
     */
    public static NanoAmount valueOfNano(BigDecimal val) {
        return valueOf(val, BASE_UNIT);
    }
    
    /**
     * Returns a NanoAmount instance representing the given Nano amount.
     *
     * <p><b>Warning:</b> This method uses the current {@link NanoUnit#BASE_UNIT} constant, which may be prone to
     * change in the future if the official Nano denomination system changes.</p>
     *
     * @param val the numeric value, in Nano (must be zero or positive)
     * @return a {@link NanoAmount} instance representing the given value
     */
    public static NanoAmount valueOfNano(long val) {
        return valueOf(val, BASE_UNIT);
    }
    
    
    /**
     * This interface is to be implemented by custom units or value denominations.
     *
     * <p>For a concrete implementation, use the constant values offered by the {@link NanoUnit} enum. You could
     * also declare your own units by constructing {@link DenominationImpl} objects.</p>
     *
     * @see NanoUnit
     * @see DenominationImpl
     */
    public interface Denomination {
        /**
         * Returns the exponent of the unit as a power of 10.
         * <p>For instance, 10<sup>x</sup>, with {@code x} being the value returned by this method.</p>
         *
         * @return the exponent of this denomination
         */
        int getExponent();
    
        /**
         * Returns the equivalent value of a single unit in {@code raw} (the smallest possible unit).
         * @return the equivalent raw value of 1 unit
         */
        BigInteger getRawValue();
    
        /**
         * Returns the friendly display name of this unit, eg: {@code Nano}.
         * @return the display name of this denomination
         */
        String getDisplayName();
    }
    
    /**
     * A simple {@link Denomination} implementation.
     */
    public static final class DenominationImpl implements Denomination {
        private final String name;
        private final int exponent;
        private final BigInteger rawVal;
    
        /**
         * Constructs a new unit Denomination constant.
         * @param name     the name of the unit
         * @param exponent the exponent of the unit's value (eg. 10<sup>x</sup>)
         */
        public DenominationImpl(String name, int exponent) {
            this.name = name;
            this.exponent = exponent;
            this.rawVal = BigInteger.TEN.pow(exponent);
        }
        
        @Override
        public int getExponent() {
            return exponent;
        }
        
        @Override
        public BigInteger getRawValue() {
            return rawVal;
        }
    
        @Override
        public String getDisplayName() {
            return name;
        }
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
