/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Karl Oczadly
 */
public class UnitHelper {
    
    public static BigDecimal convert(BigDecimal sourceAmount, int sourceExp, int destExp) {
        if (sourceAmount == null)
            throw new IllegalArgumentException("Source amount is null.");
        if (sourceAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Source amount cannot be negative.");
        if (sourceExp < 0 || destExp < 0)
            throw new IllegalArgumentException("Invalid source or destination exponent.");
        if (sourceAmount.stripTrailingZeros().scale() > sourceExp)
            throw new IllegalArgumentException("Source amount has too many decimal places.");

        BigDecimal result = sourceAmount.movePointRight(sourceExp - destExp).stripTrailingZeros();
        if (result.scale() > destExp)
            throw new ArithmeticException("Value could not be converted exactly.");
        return result;
    }
    
    public static BigInteger convertToRaw(BigDecimal sourceAmount, int sourceExp) {
        return convert(sourceAmount, sourceExp, 0).toBigIntegerExact();
    }

    public static String format(BigInteger rawAmount, NanoAmount.Denomination unit, boolean useRawIfSmall) {
        BigDecimal amount = convert(new BigDecimal(rawAmount), 0, unit.getValueExponent());
        BigDecimal scaledAmount = amount.setScale(unit.getDisplayFractionDigits(), RoundingMode.DOWN);
        boolean truncated = amount.compareTo(scaledAmount) != 0;

        // Format as raw if number is too small for precision
        if (useRawIfSmall && truncated && unit.getValueExponent() != 0
                && scaledAmount.compareTo(BigDecimal.ZERO) == 0) {
            return format(rawAmount, NanoUnit.RAW, false);
        }

        StringBuilder sb = new StringBuilder();
        // Add symbol (if applicable)
        if (unit.getSymbol() != null)
            sb.append(unit.getSymbol());
        // Format number
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
        df.setMaximumFractionDigits(unit.getValueExponent());
        df.setGroupingUsed(true);
        if (truncated) {
            df.setMinimumFractionDigits(unit.getDisplayFractionDigits());
            sb.append(df.format(scaledAmount)).append('…');
        } else {
            sb.append(df.format(scaledAmount));
        }
        // Add unit name (if not symbol)
        if (unit.getSymbol() == null)
            sb.append(" ").append(unit.getDisplayName());
        return sb.toString();
    }

}
