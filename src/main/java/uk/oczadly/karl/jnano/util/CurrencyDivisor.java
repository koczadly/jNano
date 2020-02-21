package uk.oczadly.karl.jnano.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>The unit divisions used for Nano balances. This class can be used to natively convert between the different
 * units.</p>
 * <p>If you are intending to parse or display an amount of Nano to the user, it is recommended that you use the
 * {@link #BASE_UNIT} constant, rather than explicitly specifying the unit. This constant represents the unit that
 * common users of the currency will be most familiar with.</p>
 */
public enum CurrencyDivisor {
    
    /** The largest divisor, equivalent to {@code 10^33} raw. */
    GIGA    (33,    "Gnano",     "Gxrb"),
    
    /** The 2nd largest divisor, equivalent to {@code 10^30} raw. */
    MEGA    (30,    "Nano",     "Mxrb"),
    
    /** The 3rd largest divisor, equivalent to {@code 10^27} raw. */
    KILO    (27,    "knano",     "kxrb"),
    
    /** The 4th largest divisor, equivalent to {@code 10^24} raw. */
    XRB     (24,    "nano",      "xrb"),
    
    /** The 5th largest divisor, equivalent to {@code 10^21} raw. */
    MILLI   (21,    "mnano",     "mxrb"),
    
    /** The 6th largest divisor, equivalent to {@code 10^18} raw. */
    MICRO   (18,    "μnano",     "uxrb"),
    
    /** The smallest possible unit. */
    RAW     (0,     "raw",      "raw");
    
    
    /**
     * <p>The standard base unit currently used by most services, block explorers and exchanges.</p>
     * <p>End-users are likely to be most familiar with this unit, and it is recommended that this constant is used so
     * your application can be automatically updated should the units system ever change.</p>
     * <p>As of this current version, this is equal to the {@link #MEGA} unit.</p>
     */
    public static final CurrencyDivisor BASE_UNIT = CurrencyDivisor.MEGA;
    
    
    
    int exponent;
    BigInteger value;
    String displayName, classicName;
    
    CurrencyDivisor(int exponent, String displayName, String classicName) {
        this.exponent = exponent;
        this.value = BigInteger.TEN.pow(exponent);
        this.displayName = displayName;
        this.classicName = classicName;
    }
    
    
    /**
     * <p>Returns the exponent of the unit as a power of 10.</p>
     * <p>Example: {@code 10^x}, with {@code x} being the value returned by this method.</p>
     */
    public int getExponent() {
        return exponent;
    }
    
    
    /**
     * Returns the equivalent value of a single unit for this current divisor.
     *
     * @return  the value of 1 unit
     */
    public BigInteger getValue() {
        return value;
    }
    
    
    /**
     * @return  the human-readable name for this currency unit
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the classic legacy name used within previous versions of the node.
     */
    public String getClassicName() {
        return classicName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    
    
    /**
     * Converts the given amount and unit to this unit.
     * Note that downscaling will truncate any fractional amounts.
     *
     * @param sourceAmount  the source amount to convert from
     * @param sourceUnit    the source unit to convert from
     * @return the converted value in this unit
     */
    public BigInteger convertInt(BigInteger sourceAmount, CurrencyDivisor sourceUnit) {
        return this.convert(sourceAmount, sourceUnit).toBigInteger();
    }
    
    /**
     * Converts the given amount and unit to this unit.
     * Note that downscaling will truncate any fractional amounts.
     *
     * @param sourceAmount  the source amount to convert from
     * @param sourceUnit    the source unit to convert from
     * @return the converted value in this unit
     */
    public BigInteger convertInt(BigDecimal sourceAmount, CurrencyDivisor sourceUnit) {
        return this.convert(sourceAmount, sourceUnit).toBigInteger();
    }
    
    
    /**
     * Converts the given amount and unit to this unit.
     *
     * @param sourceAmount  the source amount to convert from
     * @param sourceUnit    the source unit to convert from
     * @return the converted value in this unit
     */
    public BigDecimal convert(BigInteger sourceAmount, CurrencyDivisor sourceUnit) {
        return this.convert(new BigDecimal(sourceAmount), sourceUnit);
    }
    
    /**
     * Converts the given amount and unit to this unit.
     *
     * @param sourceAmount  the source amount to convert from
     * @param sourceUnit    the source unit to convert from
     * @return the converted value in this unit
     */
    public BigDecimal convert(BigDecimal sourceAmount, CurrencyDivisor sourceUnit) {
        // Argument checks
        if(sourceAmount == null)
            throw new IllegalArgumentException("Source amount cannot be null");
        if(sourceUnit == null)
            throw new IllegalArgumentException("Source unit cannot be null");
        if(sourceAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Source amount cannot be negative");
        
        if(sourceUnit == this)
            return sourceAmount; // Same unit
        
        if(sourceUnit.exponent > this.exponent) { // Source is higher, multiply (shift right)
            return sourceAmount.movePointRight(sourceUnit.exponent - this.exponent).stripTrailingZeros();
        } else { // Source is lower, divide (shift left)
            return sourceAmount.movePointLeft(this.exponent - sourceUnit.exponent).stripTrailingZeros();
        }
    }
    
}
