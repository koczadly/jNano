/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.model.NanoAmount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>This class represents the currency units and denominations used to represent an amount of Nano, and can be
 * used to locally convert between the different units.</p>
 *
 * <p>Alternatively, you can use the {@link NanoAmount} for representing a quantity of
 * Nano.</p>
 *
 * <p>If you are intending to parse or display an amount of Nano to the user, it is recommended that you use the
 * {@link #BASE_UNIT} constant, rather than explicitly specifying the unit. This constant represents the unit that users
 * of Nano will be most familiar with.</p>
 *
 * <p>Below are a few currency conversion examples:</p>
 * <pre>
 *   // Convert 1.337 knano (KILO) to the base unit (currently MEGA, or "Nano")
 *   BigDecimal conv1 = NanoUnit.BASE_UNIT
 *          .convertFrom(NanoUnit.KILO, new BigDecimal("1.337"));
 *   System.out.println("1.337 knano = " + conv1.toPlainString() + " Nano"); // Prints "1337 knano = 0.001337 Nano"
 *
 *   // Convert 250 unano (MICRO) to raw (RAW)
 *   BigInteger conv2 = NanoUnit.RAW
 *          .convertFromInt(NanoUnit.MICRO, BigInteger.valueOf(250));
 *   System.out.println("250 unano = " + conv2.toString() + " raw"); // "Prints 250 unano = 250000000000000000000 raw"
 * </pre>
 *
 * @see NanoAmount
 */
public enum NanoUnit {
    
    /**
     * The largest divisor, equivalent to 10<sup>33</sup> raw.
     */
    GIGA(33,  "Gnano", "Gxrb"),
    
    /**
     * The 2nd largest divisor, equivalent to 10<sup>30</sup> raw.
     */
    MEGA(30,  "Nano",  "Mxrb"),
    
    /**
     * The 3rd largest divisor, equivalent to 10<sup>27</sup> raw.
     */
    KILO(27,  "knano", "kxrb"),
    
    /**
     * The 4th largest divisor, equivalent to 10<sup>24</sup> raw.
     */
    XRB(24,   "nano",  "xrb"),
    
    /**
     * The 5th largest divisor, equivalent to 10<sup>21</sup> raw.
     */
    MILLI(21, "mnano", "mxrb"),
    
    /**
     * The 6th largest divisor, equivalent to 10<sup>18</sup> raw.
     */
    MICRO(18, "μnano", "uxrb"),
    
    /**
     * The smallest possible representable unit.
     */
    RAW(0,    "raw",   "raw");
    
    
    /**
     * <p>The standard base unit currently used by most services, block explorers and exchanges.</p>
     * <p>End-users are likely to be most familiar with this unit, and it is recommended that this constant is used so
     * your application can be automatically updated should the units system ever change.</p>
     * <p>As of this current version, this is equal to the {@link #MEGA} unit.</p>
     */
    public static final NanoUnit BASE_UNIT = NanoUnit.MEGA;
    
    private static final DecimalFormat FRIENDLY_DF = new DecimalFormat("#,##0.######");
    private static final DecimalFormat FRIENDLY_DF_FORCE = new DecimalFormat("#,##0.000000");
    
    final int exponent;
    final BigInteger rawValue;
    final String displayName, classicName;
    
    NanoUnit(int exponent, String displayName, String classicName) {
        this.exponent = exponent;
        this.rawValue = BigInteger.TEN.pow(exponent);
        this.displayName = displayName;
        this.classicName = classicName;
    }
    
    
    /**
     * <p>Returns the exponent of the unit as a power of 10.</p>
     * <p>For instance, 10<sup>x</sup>, with {@code x} being the value returned by this method.</p>
     * @return the exponent of this unit
     */
    public int getExponent() {
        return exponent;
    }
    
    
    /**
     * Returns the equivalent value of a single unit in raw.
     *
     * @return the equivalent raw value of 1 unit
     */
    public BigInteger getRawValue() {
        return rawValue;
    }
    
    
    /**
     * Returns the human-readable name for this currency unit.
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the classic legacy name used within previous versions of the node.
     * @return the legacy name
     */
    public String getClassicName() {
        return classicName;
    }
    
    
    @Override
    public String toString() {
        return getDisplayName();
    }
    
    
    /**
     * <p>Converts the specified unit and amount into this unit.</p>
     * <p>If you are converting from a smaller unit and fractional digits are lost, then an {@link ArithmeticException}
     * will be thrown. If you wish to bypass this, use {@link #convertFrom(NanoUnit, BigInteger)} and transform
     * the retrieved value into a BigInteger using the {@link BigDecimal#toBigInteger()} method.</p>
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     *
     * @throws ArithmeticException if the conversion would result in a loss of information
     */
    public BigInteger convertFromInt(NanoUnit sourceUnit, BigInteger sourceAmount) {
        return convertFromInt(sourceUnit, new BigDecimal(sourceAmount));
    }
    
    /**
     * <p>Converts the specified unit and amount into this unit.</p>
     * <p>If you are converting from a smaller unit and fractional digits are lost, then an {@link ArithmeticException}
     * will be thrown. If you wish to bypass this, use {@link #convertFrom(NanoUnit, BigDecimal)} and transform
     * the retrieved value into a BigInteger using the {@link BigDecimal#toBigInteger()} method.</p>
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     *
     * @throws ArithmeticException if the conversion would result in a loss of information, or if the {@code
     * sourceAmount} has too many decimal digits for the specified {@code sourceUnit}
     */
    public BigInteger convertFromInt(NanoUnit sourceUnit, BigDecimal sourceAmount) {
        BigDecimal decimalVal = this.convertFrom(sourceUnit, sourceAmount);
        try {
            return decimalVal.toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw new ArithmeticException(
                    String.format("Converting %s %s to %s is not permitted, as fractional amounts would be truncated." +
                                    " Use convert(sourceAmount, sourceUnit).toBigInteger() if you are okay with " +
                                    "losing this information.",
                            sourceAmount, sourceUnit.getDisplayName(), this.getDisplayName()));
        }
    }
    
    
    /**
     * Converts the specified unit and amount into this unit.
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     */
    public BigDecimal convertFrom(NanoUnit sourceUnit, BigInteger sourceAmount) {
        return this.convertFrom(sourceUnit, new BigDecimal(sourceAmount));
    }
    
    /**
     * Converts the specified unit and amount into this unit.
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     * @throws ArithmeticException if the {@code sourceAmount} has too many decimal digits for the specified
     * {@code sourceUnit}
     */
    public BigDecimal convertFrom(NanoUnit sourceUnit, BigDecimal sourceAmount) {
        // Argument checks
        if (sourceAmount == null)
            throw new IllegalArgumentException("Source amount cannot be null");
        if (sourceUnit == null)
            throw new IllegalArgumentException("Source unit cannot be null");
        if (sourceAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Source amount cannot be negative");
        sourceAmount = sourceAmount.stripTrailingZeros();
        if (sourceAmount.scale() > sourceUnit.exponent)
            throw new ArithmeticException("Source amount scale is too large for the specified source unit.");
    
        BigDecimal result;
        if (sourceUnit == this) {
            // Same unit
            result = sourceAmount;
        } else if (sourceUnit.exponent > this.exponent) {
            // Source is higher, multiply (shift decimal right)
            result = sourceAmount.movePointRight(sourceUnit.exponent - this.exponent);
        } else {
            // Source is lower, divide (shift decimal left)
            result = sourceAmount.movePointLeft(this.exponent - sourceUnit.exponent);
        }
        return result.setScale(this.exponent, RoundingMode.FLOOR);
    }
    
    /**
     * <p>Converts a given value of <i>raw</i> to the current base unit ({@link #BASE_UNIT}), and formats the number to
     * up to 6 decimal places (cutting off truncated digits), along with a suffix of the unit name. The value will
     * also be formatted to contain separating commas for every 3 digits.</p>
     *
     * <p>For instance, a value of {@code 1234567000000000000000000000000001} will return
     * <code>1,234.567000&#8230; Nano</code>.</p>
     *
     * <p>This value should not be used for any computations, and should only be used for displaying quantities of
     * the currency to a user.</p>
     * @param rawAmount the amount of raw to convert from
     * @return a friendly string of a given currency amount
     */
    public static String toFriendlyString(BigInteger rawAmount) {
        return toFriendlyString(new BigDecimal(rawAmount), RAW);
    }
    
    /**
     * <p>Converts a given quantity of Nano to the current base unit ({@link #BASE_UNIT}), and formats the number to
     * up to 6 decimal places (cutting off truncated digits), along with a suffix of the unit name. The value will
     * also be formatted to contain separating commas for every 3 digits.</p>
     *
     * <p>For instance, a value of {@code 1234567000000000000000000000000001} {@link #RAW} will return
     * <code>1,234.567000&#8230; Nano</code>.</p>
     *
     * <p>This value should not be used for any computations, and should only be used for displaying quantities of
     * the currency to a user.</p>
     * @param amount     the amount to convert from
     * @param sourceUnit the source unit of the amount to convert from
     * @return a friendly string of a given currency amount
     */
    public static String toFriendlyString(BigDecimal amount, NanoUnit sourceUnit) {
        BigDecimal nanoAmount = BASE_UNIT.convertFrom(sourceUnit, amount);
        BigDecimal scaledAmount = nanoAmount.setScale(6, RoundingMode.FLOOR);
        boolean trimmed = nanoAmount.compareTo(scaledAmount) != 0;
    
        String valStr;
        if (scaledAmount.compareTo(BigDecimal.ZERO) == 0) {
            valStr = trimmed ? ">0" : "0";
        } else {
            if (trimmed) {
                valStr = FRIENDLY_DF_FORCE.format(scaledAmount) + ((char)8230); // Ellipsis character
            } else {
                valStr = FRIENDLY_DF.format(scaledAmount);
            }
        }
        return valStr + " " + BASE_UNIT.getDisplayName();
    }
    
}
