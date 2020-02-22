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
                CurrencyDivisor.RAW, new BigDecimal("25000000000000000000000000000000000"))
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("25.000000000000000000000000000000001", CurrencyDivisor.GIGA.convertFrom(
                CurrencyDivisor.RAW, new BigDecimal("25000000000000000000000000000000001"))
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("24.999999999999999999999999999999999", CurrencyDivisor.GIGA.convertFrom(
                CurrencyDivisor.RAW, new BigDecimal("24999999999999999999999999999999999"))
                .stripTrailingZeros().toPlainString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convertFrom(
                CurrencyDivisor.MILLI, new BigDecimal("2000"))
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testDecimalConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convertFrom(
                CurrencyDivisor.GIGA, new BigDecimal(25))
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convertFrom(
                CurrencyDivisor.GIGA, BigDecimal.ONE)
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convertFrom(
                CurrencyDivisor.GIGA, BigDecimal.ONE)
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testLossyIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                CurrencyDivisor.GIGA.convertIntFrom(
                        CurrencyDivisor.RAW, new BigInteger("25000000000000000000000000000000001")));
    
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                CurrencyDivisor.GIGA.convertIntFrom(
                        CurrencyDivisor.RAW, new BigInteger("24999999999999999999999999999999999")));
    }
    
    @Test
    public void testIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convertIntFrom(
                CurrencyDivisor.RAW, new BigInteger("25000000000000000000000000000000000"))
                .toString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convertIntFrom(
                CurrencyDivisor.MILLI, new BigInteger("2000"))
                .toString());
    }
    
    @Test
    public void testIntegerConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convertIntFrom(
                CurrencyDivisor.GIGA, BigInteger.valueOf(25))
                .toString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convertIntFrom(
                CurrencyDivisor.GIGA, BigInteger.ONE)
                .toString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convertIntFrom(
                CurrencyDivisor.GIGA, BigInteger.ONE)
                .toString());
    }
    
}