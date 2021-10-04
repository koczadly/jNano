/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import uk.oczadly.karl.jnano.model.NanoAmount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
        
        BigDecimal result;
        if (sourceExp == destExp) {
            // Same unit
            return sourceAmount;
        } else if (sourceExp > destExp) {
            // Source is higher, multiply (shift decimal right)
            result = sourceAmount.movePointRight(sourceExp - destExp);
        } else {
            // Source is lower, divide (shift decimal left)
            result = sourceAmount.movePointLeft(destExp - sourceExp);
        }
        
        result = result.stripTrailingZeros();
        if (result.scale() > destExp)
            throw new ArithmeticException("Value could not be converted exactly.");
        return result;
    }
    
    public static BigInteger convertToRaw(BigDecimal sourceAmount, int sourceExp) {
        return convert(sourceAmount, sourceExp, 0).toBigIntegerExact();
    }
    
    
    private static final DecimalFormat FRIENDLY_DF = new DecimalFormat("#,##0.######");
    private static final DecimalFormat FRIENDLY_DF_FORCE = new DecimalFormat("#,##0.000000");
    private static final Object dfMutex = new Object(); // DecimalFormat aint thread safe
    
    public static String toFriendlyString(BigDecimal amount, NanoAmount.Denomination unit) {
        BigDecimal scaledAmount = amount.setScale(6, RoundingMode.DOWN);
        boolean trimmed = amount.compareTo(scaledAmount) != 0;
        
        StringBuilder sb = new StringBuilder();
        if (unit.getSymbol() != null)
            sb.append(unit.getSymbol());
        // Format amount
        if (scaledAmount.compareTo(BigDecimal.ZERO) == 0) {
            sb.append('0');
            if (trimmed)
                sb.appendCodePoint(8230);
        } else {
            synchronized (dfMutex) {
                if (trimmed) {
                    sb.append(FRIENDLY_DF_FORCE.format(scaledAmount));
                    sb.appendCodePoint(8230); // Ellipsis character
                } else {
                    sb.append(FRIENDLY_DF.format(scaledAmount));
                }
            }
        }
        // Add unit name (if not symbol)
        if (unit.getSymbol() == null)
            sb.append(" ").append(unit.getDisplayName());
        return sb.toString();
    }

}
