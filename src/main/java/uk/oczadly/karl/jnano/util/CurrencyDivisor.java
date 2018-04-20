package uk.oczadly.karl.jnano.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The unit divisions used for Nano balances.
 *
 * This class can be used to natively convert between the different units.
 */
public enum CurrencyDivisor {
    
    /** The largest divisor, equal to 10^33 */
    GIGA    (33,    "Gxrb",     "Gxrb"),
    
    /** The 2nd largest divisor, equal to 10^30 */
    MEGA    (30,    "Nano",     "Mxrb"),
    
    /** The 3rd largest divisor, equal to 10^27 */
    KILO    (27,    "kxrb",     "kxrb"),
    
    /** The 4th largest divisor, equal to 10^24 */
    XRB     (24,    "xrb",      "xrb"),
    
    /** The 5th largest divisor, equal to 10^21 */
    MILLI   (21,    "mxrb",     "mxrb"),
    
    /** The 2nd smallest divisor, equal to 10^18 */
    MICRO   (18,    "uxrb",     "uxrb"),
    
    /** The smallest divisor, equal to 10^0 */
    RAW     (0,     "raw",      "raw");
    
    
    /**
     * The standard base unit currently used
     */
    public static final CurrencyDivisor BASE_UNIT = CurrencyDivisor.MEGA;
    
    
    
    private int exponent;
    private BigInteger value;
    private String displayName, classicName;
    
    CurrencyDivisor(int exponent, String displayName, String classicName) {
        this.exponent = exponent;
        this.value = BigInteger.TEN.pow(exponent);
        this.displayName = displayName;
        this.classicName = classicName;
    }
    
    
    /**
     * @return  the exponent of the unit as a power of 10
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
     * @return  the classic name for this currency unit
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
        //Argument checks
        if(sourceAmount == null) throw new IllegalArgumentException("Source amount cannot be null");
        if(sourceUnit == null) throw new IllegalArgumentException("Source unit cannot be null");
        if(sourceAmount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Source amount cannot be negative");
        
        if(sourceUnit == this) return sourceAmount; //Same unit
        
        if(sourceUnit.exponent > this.exponent) { //Source is higher, multiply (shift right)
            return sourceAmount.movePointRight(sourceUnit.exponent - this.exponent).stripTrailingZeros();
        } else { //Source is lower, divide (shift left)
            return sourceAmount.movePointLeft(this.exponent - sourceUnit.exponent).stripTrailingZeros();
        }
    }
    
}
