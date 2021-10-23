/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.internal.utils.UnitHelper;
import uk.oczadly.karl.jnano.model.NanoAmount;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>This class represents the currency units and denominations used to represent an amount of Nano, and can be
 * used to locally convert between the different units. Alternatively, you can use the {@link NanoAmount} class to
 * represent a quantity of Nano and perform unit conversions.</p>
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
 *   System.out.println(conv1.toPlainString()); // Prints "0.001337"
 *
 *   // Convert 250 unano (MICRO) to raw (RAW)
 *   BigInteger conv2 = NanoUnit.RAW
 *          .convertFromInt(NanoUnit.MICRO, BigInteger.valueOf(250));
 *   System.out.println("250 unano = " + conv2.toString() + " raw"); // "Prints 250 unano = 250000000000000000000 raw"
 * </pre>
 *
 * @see NanoAmount
 */
public enum NanoUnit implements NanoAmount.Denomination {
    
    /**
     * The largest divisor, equivalent to 10<sup>33</sup> raw.
     *
     * @deprecated This currency unit is seldom used, and use of it is discouraged to prevent confusion.
     */
    @Deprecated
    GIGA  (33, 6, "Gnano*", null),
    
    /**
     * The 2nd largest divisor, equivalent to 10<sup>30</sup> raw.
     */
    MEGA  (30, 6, "nano",  "Ӿ"),
    
    /**
     * The 3rd largest divisor, equivalent to 10<sup>27</sup> raw.
     *
     * @deprecated This currency unit is seldom used, and use of it is discouraged to prevent confusion.
     */
    @Deprecated
    KILO  (27, 3, "knano*", null),
    
    /**
     * The 4th largest divisor, equivalent to 10<sup>24</sup> raw.
     *
     * @deprecated This currency unit is seldom used, and use of it is discouraged to prevent confusion.
     */
    @Deprecated
    XRB   (24, 3, "nano*",  null),
    
    /**
     * The 5th largest divisor, equivalent to 10<sup>21</sup> raw.
     *
     * @deprecated This currency unit is seldom used, and use of it is discouraged to prevent confusion.
     */
    @Deprecated
    MILLI (21, 3, "mnano*", null),
    
    /**
     * The 6th largest divisor, equivalent to 10<sup>18</sup> raw.
     *
     * @deprecated This currency unit is seldom used, and use of it is discouraged to prevent confusion.
     */
    @Deprecated
    MICRO (18, 3, "μnano*", null),
    
    /**
     * The smallest possible representable unit.
     */
    RAW   (0,  0,  "raw",   null);
    
    /**
     * The standard base unit currently used by most services, block explorers and exchanges.
     *
     * <p>End-users are likely to be most familiar with this unit. Be aware that this constant may change at any time,
     * following any future adjustments made to the official units system. Any amount values should be stored in
     * the {@link #RAW raw} unit.</p>
     *
     * <p>As of now, this is equal to the {@link #MEGA} unit.</p>
     */
    public static final NanoUnit BASE_UNIT = NanoUnit.MEGA;
    
    
    final int exponent, formatDecimals;
    final BigInteger rawValue;
    final String displayName, symbol;
    
    NanoUnit(int exponent, int formatDecimals, String displayName, String symbol) {
        this.exponent = exponent;
        this.rawValue = BigInteger.TEN.pow(exponent);
        this.formatDecimals = formatDecimals;
        this.displayName = displayName;
        this.symbol = symbol;
    }
    
    
    @Override
    public int getValueExponent() {
        return exponent;
    }
    
    @Override
    public BigInteger getRawValue() {
        return rawValue;
    }
    
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public int getDisplayFractionDigits() {
        return formatDecimals;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
    
    
    /**
     * Converts the specified unit and amount into this unit.
     *
     * <p>If you are converting from a smaller unit and fractional digits are lost, then an {@link ArithmeticException}
     * will be thrown. If you wish to bypass this, use {@link #convertFrom(NanoUnit, BigInteger)} and
     * transform the retrieved value into a BigInteger using the {@link BigDecimal#toBigInteger()} method.</p>
     *
     * <p>The {@link NanoAmount} class and it's provided conversion methods are recommended over this method for better
     * code clarity.</p>
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
     * Converts the specified unit and amount into this unit.
     *
     * <p>If you are converting from a smaller unit and fractional digits are lost, then an {@link ArithmeticException}
     * will be thrown. If you wish to bypass this, use {@link #convertFrom(NanoUnit, BigDecimal)} and
     * transform the retrieved value into a BigInteger using the {@link BigDecimal#toBigInteger()} method.</p>
     *
     * <p>The {@link NanoAmount} class and it's provided conversion methods are recommended over this method for better
     * code clarity.</p>
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     *
     * @throws ArithmeticException if the conversion would result in a loss of information, or if the {@code
     * sourceAmount} has too many decimal digits for the specified {@code sourceUnit}
     */
    public BigInteger convertFromInt(NanoUnit sourceUnit, BigDecimal sourceAmount) {
        BigDecimal decimalVal = convertFrom(sourceUnit, sourceAmount);
        try {
            return decimalVal.toBigIntegerExact();
        } catch (ArithmeticException e) {
            throw new ArithmeticException(String.format(
                    "Conversion of %s to %s was not possible without losing information.",
                    sourceUnit.getDisplayName(), getDisplayName()));
        }
    }
    
    
    /**
     * Converts the specified unit and amount into this unit.
     *
     * <p>The {@link NanoAmount} class and it's provided conversion methods are recommended over this method for better
     * code clarity.</p>
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     */
    public BigDecimal convertFrom(NanoUnit sourceUnit, BigInteger sourceAmount) {
        return convertFrom(sourceUnit, new BigDecimal(sourceAmount));
    }
    
    /**
     * Converts the specified unit and amount into this unit.
     *
     * <p>The {@link NanoAmount} class and it's provided conversion methods are recommended over this method for better
     * code clarity.</p>
     *
     * @param sourceAmount the source amount to convert from
     * @param sourceUnit   the source unit to convert from
     * @return the converted value in this unit
     * @throws ArithmeticException if the {@code sourceAmount} has too many decimal digits for the specified
     * {@code sourceUnit}
     */
    public BigDecimal convertFrom(NanoUnit sourceUnit, BigDecimal sourceAmount) {
        if (sourceAmount == null)
            throw new IllegalArgumentException("Source amount cannot be null");
        if (sourceUnit == null)
            throw new IllegalArgumentException("Source unit cannot be null");
        if (sourceAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Source amount cannot be negative");
        
        return UnitHelper.convert(sourceAmount, sourceUnit.exponent, exponent);
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
     *
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
     *
     * @param amount     the amount to convert from
     * @param sourceUnit the source unit of the amount to convert from
     * @return a friendly string of a given currency amount
     */
    public static String toFriendlyString(BigDecimal amount, NanoUnit sourceUnit) {
        return UnitHelper.format(RAW.convertFromInt(sourceUnit, amount), BASE_UNIT, false);
    }
    
}
