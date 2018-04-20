package uk.oczadly.karl.jnano.util;

import uk.oczadly.karl.jnano.tests.UtilTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

@Category(UtilTests.class)
public class CurrencyDivisorTest {
    
    @Test
    public void testValues() {
        assertEquals("1000000000000000000000000000000000", CurrencyDivisor.GIGA.getValue().toString());
        assertEquals("1000000000000000000000000", CurrencyDivisor.XRB.getValue().toString());
        assertEquals("1", CurrencyDivisor.RAW.getValue().toString());
    }
    
    
    @Test
    public void testDecimalConversionUpscale() {
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convert(new BigDecimal("25000000000000000000000000000000000"), CurrencyDivisor.RAW).stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("25.000000000000000000000000000000001", CurrencyDivisor.GIGA.convert(new BigDecimal("25000000000000000000000000000000001"), CurrencyDivisor.RAW).stripTrailingZeros().toPlainString());
        
        //Convert RAW to GIGA
        assertEquals("24.999999999999999999999999999999999", CurrencyDivisor.GIGA.convert(new BigDecimal("24999999999999999999999999999999999"), CurrencyDivisor.RAW).stripTrailingZeros().toPlainString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convert(new BigDecimal("2000"), CurrencyDivisor.MILLI).stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testDecimalConversionDownscale() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convert(new BigDecimal(25), CurrencyDivisor.GIGA).stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convert(BigDecimal.ONE, CurrencyDivisor.GIGA).stripTrailingZeros().toPlainString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convert(BigDecimal.ONE, CurrencyDivisor.GIGA).stripTrailingZeros().toPlainString());
    }
    
    
    @Test
    public void testIntegerConversionUpscale() {
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convertInt(new BigInteger("25000000000000000000000000000000000"), CurrencyDivisor.RAW).toString());
        
        //Convert RAW to GIGA
        assertEquals("25", CurrencyDivisor.GIGA.convertInt(new BigInteger("25000000000000000000000000000000001"), CurrencyDivisor.RAW).toString());
        
        //Convert RAW to GIGA
        assertEquals("24", CurrencyDivisor.GIGA.convertInt(new BigInteger("24999999999999999999999999999999999"), CurrencyDivisor.RAW).toString());
        
        //Convert MILLI to XRB
        assertEquals("2", CurrencyDivisor.XRB.convertInt(new BigInteger("2000"), CurrencyDivisor.MILLI).toString());
    }
    
    @Test
    public void testIntegerConversionDownscale() {
        //Convert 25 GIGA to RAW
        assertEquals("25000000000000000000000000000000000", CurrencyDivisor.RAW.convertInt(BigInteger.valueOf(25), CurrencyDivisor.GIGA).toString());
        
        //Convert 1 GIGA to MEGA
        assertEquals("1000", CurrencyDivisor.MEGA.convertInt(BigInteger.ONE, CurrencyDivisor.GIGA).toString());
        
        //Convert 1 GIGA to KILO
        assertEquals("1000000", CurrencyDivisor.KILO.convertInt(BigInteger.ONE, CurrencyDivisor.GIGA).toString());
    }
    
}