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
 * An immutable class which represents a quantity of Nano, backed by a {@link BigInteger} raw value.
 *
 * <p>This class can be used as a transactional amount or account balance. Only unsigned values are supported, so this
 * cannot be used to represent a negative quantity.</p>
 *
 * <p>To create an instance of this class, use one of the provided static {@code valueOf} methods, or one of the
 * constant field values. This class also supports built-in Gson serialization and deserialization as a raw value.</p>
 *
 * <p>Custom units can be specified by implementing the {@link Denomination} interface, or using one of the static
 * creation methods provided by the class. For instance, to create the unit for Banano you could define the following
 * constant:</p>
 * <pre>{@code // Static constant representing the standard Banano unit
 * public static final NanoAmount.Denomination UNIT_BAN = NanoAmount.Denomination.create(29, "BAN");
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

    /**
     * A zero-value amount.
     */
    public static final NanoAmount ZERO = new NanoAmount(BigInteger.ZERO);

    /**
     * The maximum possible balance value, equal to <code>2<sup>128</sup>-1</code>.
     *
     * <p>This is also the balance value of the genesis block.</p>
     */
    public static final NanoAmount MAX_VALUE = new NanoAmount(MAX_VAL_RAW);

    /**
     * A constant value representing a single {@link NanoUnit#RAW raw} unit ({@code 1 raw}).
     *
     * <p>This is the smallest representable quantity of Nano possible.</p>
     */
    public static final NanoAmount ONE_RAW = new NanoAmount(BigInteger.ONE);

    /**
     * A constant value representing a single {@link NanoUnit#BASE_UNIT Nano} unit ({@code 1 Nano}).
     *
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
        if (unit == null) throw new IllegalArgumentException("Destination unit cannot be null.");
        if (unit == BASE_UNIT && cachedBaseUnitValue != null)
            return cachedBaseUnitValue; // Return from cache
        BigDecimal amount = UnitHelper.convert(new BigDecimal(rawValue), 0, unit.getValueExponent());
        if (unit == BASE_UNIT)
            cachedBaseUnitValue = amount; // Store in cache
        return amount;
    }


    /**
     * Returns this amount as a friendly string, complete with the unit name. The value will be formatted either in the
     * {@link NanoUnit#BASE_UNIT base unit}, or {@link NanoUnit#RAW raw} depending on which is more suitable.
     *
     * @return a friendly string of this amount
     */
    @Override
    public String toString() {
        return UnitHelper.format(rawValue, BASE_UNIT, true);
    }

    /**
     * Returns this amount as a friendly string, including the symbol or unit name if no symbol is provided.
     *
     * @param unit the unit to display the amount in
     * @return a friendly string representing this amount
     */
    public String toString(Denomination unit) {
        return UnitHelper.format(rawValue, unit, false);
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
        return Objects.equals(rawValue, ((NanoAmount) o).rawValue);
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
        if (unit.getValueExponent() == 0)
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
        if (unit.getValueExponent() == 0)
            return valueOfRaw(val); // Skip conversion if source unit is raw
        return valueOf(new BigDecimal(val), unit);
    }

    /**
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified {@code
     *                                  unit}
     */
    public static NanoAmount valueOf(BigDecimal val, Denomination unit) {
        if (unit == BASE_UNIT) {
            return new NanoAmount(UnitHelper.convertToRaw(val, unit.getValueExponent()), val);
        } else {
            return new NanoAmount(UnitHelper.convertToRaw(val, unit.getValueExponent()));
        }
    }

    /**
     * Returns a NanoAmount that represents the given amount and unit.
     *
     * @param val  the numeric (integer or decimal) value (must be zero or positive)
     * @param unit the denomination of the {@code val} amount
     * @return a {@link NanoAmount} instance representing the given value
     * @throws NumberFormatException    if val is not a valid decimal number
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified {@code
     *                                  unit}
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
     * Returns a NanoAmount that represents <code>10<sup>exponent</sup></code> {@link NanoUnit#RAW raw}. An {@code
     * exponent} value of {@code 5} means one followed by five zeroes ({@code 100000 raw}). Useful for constructing
     * threshold values.
     *
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
     * @throws NumberFormatException    if val is not a valid integer or decimal number
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified {@code
     *                                  unit}
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
     * @throws IllegalArgumentException if the source {@code val} has too many decimal digits for the specified {@code
     *                                  unit}
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


    /**
     * This interface is to be implemented by custom units or value denominations.
     *
     * <p>This interface only supports the use of denominations of a power of 10, as represented by the exponent.</p>
     *
     * <p>For a concrete implementation, use the constant values offered by the {@link NanoUnit} enum. You could
     * also declare your own units by calling one of the static {@code create(…)} methods.</p>
     *
     * @see NanoUnit
     * @see #create(int, int, String, String)
     */
    public interface Denomination {
        /**
         * Returns the exponent of the value of this unit as a power of 10.
         * <p>For instance, <code>10<sup>x</sup></code>, with {@code x} being the value returned by this method.</p>
         *
         * @return the exponent of this denomination
         */
        int getValueExponent();

        /**
         * Returns the friendly display name of this unit, eg: {@code Nano}.
         *
         * @return the display name of this denomination
         */
        String getDisplayName();

        /**
         * Returns the equivalent value of a single unit in {@code raw} (the smallest possible unit).
         *
         * @return the equivalent raw value of 1 unit
         */
        default BigInteger getRawValue() {
            return BigInteger.TEN.pow(getValueExponent());
        }

        /**
         * Returns the prefix symbol of this currency. In most cases, this should be a single symbolic character.
         *
         * @return the prefix symbol, or null if this unit has no symbol
         */
        default String getSymbol() {
            return null;
        }

        /**
         * Returns the <em>suggested</em> number of decimal places to display when formatted as a string.
         *
         * <p>Defaults to {@code min(6, exponent)} if not overridden.</p>
         *
         * @return the suggested number of decimal places
         */
        default int getDisplayFractionDigits() {
            return calculateFractionDigits(getValueExponent());
        }

        /**
         * Creates a custom unit {@link Denomination} object from the given parameters.
         *
         * @param exponent the value of this unit in raw, as a power of 10 (<code>10<sup>x</sup></code>, where {@code x}
         *                 is the value being passed
         * @param name     the display name of the unit (eg. "{@code Nano}")
         * @return the custom denomination object
         */
        static Denomination create(int exponent, String name) {
            return create(exponent, calculateFractionDigits(exponent), name, null);
        }

        /**
         * Creates a custom unit {@link Denomination} object from the given parameters.
         *
         * @param exponent       the value of this unit in raw, as a power of 10 (<code>10<sup>x</sup></code>, where
         *                       {@code x} is the value being passed
         * @param formatDecimals the suggested number of decimal places to show when formatted as a string
         * @param name           the display name of the unit (eg. "{@code Nano}")
         * @param symbol         the symbolic character(s) which prefixes the decimal amount (eg. "{@code $}")
         * @return the custom denomination object
         */
        static Denomination create(int exponent, int formatDecimals, String name, String symbol) {
            if (exponent < 0 || exponent >= 39)
                throw new IllegalArgumentException("Unit exponent out of possible range.");
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Unit name cannot be null or empty.");
            return new DenominationImpl(exponent, formatDecimals, name, symbol);
        }
    }

    private static class DenominationImpl implements Denomination {
        private final int exponent, fractionDigits;
        private final BigInteger rawVal;
        private final String name, symbol;

        DenominationImpl(int exponent, int fractionDigits, String name, String symbol) {
            this.exponent = exponent;
            this.rawVal = BigInteger.TEN.pow(exponent);
            this.fractionDigits = fractionDigits;
            this.name = name;
            this.symbol = symbol;
        }

        @Override
        public int getValueExponent() {
            return exponent;
        }

        @Override
        public BigInteger getRawValue() {
            return rawVal;
        }

        @Override
        public int getDisplayFractionDigits() {
            return fractionDigits;
        }

        @Override
        public String getDisplayName() {
            return name;
        }

        @Override
        public String getSymbol() {
            return symbol;
        }
    }

    private static int calculateFractionDigits(int exponent) {
        return Math.min(6, exponent);
    }

}
