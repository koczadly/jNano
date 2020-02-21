package uk.oczadly.karl.jnano.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CurrencyDivisorTest {
    
    @Test
    public void testValues() {
        assertEquals("1000000000000000000000000000000000", CurrencyDivisor.GIGA.getRawValue().toString());
        assertEquals("1000000000000000000000000", CurrencyDivisor.XRB.getRawValue().toString());
        assertEquals("1", CurrencyDivisor.RAW.getRawValue().toString());
    }
    
    
    @Test
    public void testDecimalConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convertFrom(
                new BigDecimal("25000000000000000000000000000000000"), CurrencyDivisor.RAW)
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("25.000000000000000000000000000000001", CurrencyDivisor.GIGA.convertFrom(
                new BigDecimal("25000000000000000000000000000000001"), CurrencyDivisor.RAW)
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("24.999999999999999999999999999999999", CurrencyDivisor.GIGA.convertFrom(
                new BigDecimal("24999999999999999999999999999999999"), CurrencyDivisor.RAW)
                .stripTrailingZeros().toPlainString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convertFrom(
                new BigDecimal("2000"), CurrencyDivisor.MILLI)
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testDecimalConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convertFrom(
                new BigDecimal(25), CurrencyDivisor.GIGA)
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convertFrom(
                BigDecimal.ONE, CurrencyDivisor.GIGA)
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convertFrom(
                BigDecimal.ONE, CurrencyDivisor.GIGA)
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testLossyIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                CurrencyDivisor.GIGA.convertIntFrom(
                        new BigInteger("25000000000000000000000000000000001"), CurrencyDivisor.RAW));
    
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                CurrencyDivisor.GIGA.convertIntFrom(
                        new BigInteger("24999999999999999999999999999999999"), CurrencyDivisor.RAW));
    }
    
    @Test
    public void testIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convertIntFrom(
                new BigInteger("25000000000000000000000000000000000"), CurrencyDivisor.RAW)
                .toString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convertIntFrom(
                new BigInteger("2000"), CurrencyDivisor.MILLI)
                .toString());
    }
    
    @Test
    public void testIntegerConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convertIntFrom(
                BigInteger.valueOf(25), CurrencyDivisor.GIGA)
                .toString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convertIntFrom(
                BigInteger.ONE, CurrencyDivisor.GIGA)
                .toString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convertIntFrom(
                BigInteger.ONE, CurrencyDivisor.GIGA)
                .toString());
    }
    
}