package uk.oczadly.karl.jnano.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NanoUnitTest {
    
    @Test
    public void testValues() {
        assertEquals("1000000000000000000000000000000000", NanoUnit.GIGA.getRawValue().toString());
        assertEquals("1000000000000000000000000", NanoUnit.XRB.getRawValue().toString());
        assertEquals("1", NanoUnit.RAW.getRawValue().toString());
    }
    
    
    @Test
    public void testDecimalConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertEquals("25", NanoUnit.GIGA.convertFrom(
                NanoUnit.RAW, new BigDecimal("25000000000000000000000000000000000"))
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("25.000000000000000000000000000000001", NanoUnit.GIGA.convertFrom(
                NanoUnit.RAW, new BigDecimal("25000000000000000000000000000000001"))
                .stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("24.999999999999999999999999999999999", NanoUnit.GIGA.convertFrom(
                NanoUnit.RAW, new BigDecimal("24999999999999999999999999999999999"))
                .stripTrailingZeros().toPlainString());
        
        //Convert MILLI to XRB
        assertEquals("2", NanoUnit.XRB.convertFrom(
                NanoUnit.MILLI, new BigDecimal("2000"))
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testDecimalConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", NanoUnit.RAW.convertFrom(
                NanoUnit.GIGA, new BigDecimal(25))
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", NanoUnit.MEGA.convertFrom(
                NanoUnit.GIGA, BigDecimal.ONE)
                .stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", NanoUnit.KILO.convertFrom(
                NanoUnit.GIGA, BigDecimal.ONE)
                .stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testLossyIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                NanoUnit.GIGA.convertFromInt(
                        NanoUnit.RAW, new BigInteger("25000000000000000000000000000000001")));
    
        //Convert RAW to GIGA
        assertThrows(ArithmeticException.class, () ->
                NanoUnit.GIGA.convertFromInt(
                        NanoUnit.RAW, new BigInteger("24999999999999999999999999999999999")));
    }
    
    @Test
    public void testIntegerConversionToLargerUnit() {
        //Convert RAW to GIGA
        assertEquals("25", NanoUnit.GIGA.convertFromInt(
                NanoUnit.RAW, new BigInteger("25000000000000000000000000000000000"))
                .toString());
        
        //Convert MILLI to XRB
        assertEquals("2", NanoUnit.XRB.convertFromInt(
                NanoUnit.MILLI, new BigInteger("2000"))
                .toString());
    }
    
    @Test
    public void testIntegerConversionToSmallerUnit() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", NanoUnit.RAW.convertFromInt(
                NanoUnit.GIGA, BigInteger.valueOf(25))
                .toString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", NanoUnit.MEGA.convertFromInt(
                NanoUnit.GIGA, BigInteger.ONE)
                .toString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", NanoUnit.KILO.convertFromInt(
                NanoUnit.GIGA, BigInteger.ONE)
                .toString());
    }
    
    @Test
    public void testFriendlyName() {
        assertEquals("1,234.567001 Nano",
                NanoUnit.toFriendlyString(new BigInteger("1234567000000000000000000000000001")));
    }
    
}