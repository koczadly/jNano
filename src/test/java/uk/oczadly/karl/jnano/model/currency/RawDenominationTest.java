package uk.oczadly.karl.jnano.model.currency;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static org.junit.Assert.*;

public class RawDenominationTest {

    static final Denomination RAW = new RawDenomination("raw");

    static final String NANO_RAW_STR = "1000000000000000000000000000000";
    static final BigInteger NANO_RAW = new BigInteger(NANO_RAW_STR);
    static final Locale LOCALE = Locale.ROOT;


    @Test
    public void testConvertFromRaw() {
        assertEquals(BigDecimal.ZERO, RAW.convertFromRaw(BigInteger.ZERO));
        assertEquals(BigDecimal.ONE, RAW.convertFromRaw(BigInteger.ONE));
        assertEquals(new BigDecimal(NANO_RAW), RAW.convertFromRaw(NANO_RAW));
    }

    @Test
    public void testConvertToRaw() {
        assertEquals(BigInteger.ZERO, RAW.convertToRaw(BigDecimal.ZERO));
        assertEquals(BigInteger.ONE, RAW.convertToRaw(BigDecimal.ONE));
        assertEquals(BigInteger.ONE, RAW.convertToRaw(new BigDecimal("1.0000000000000")));
        assertEquals(NANO_RAW, RAW.convertToRaw(new BigDecimal(NANO_RAW)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertToRawPrecisionTooHigh() {
        RAW.convertToRaw(new BigDecimal("1.1"));
    }

    @Test
    public void testFormat() {
        assertEquals("0 raw", RAW.format(BigDecimal.ZERO, LOCALE));
        assertEquals("1 raw", RAW.format(BigDecimal.ONE, LOCALE));
        assertEquals(NANO_RAW_STR + " raw", RAW.format(new BigDecimal(NANO_RAW), LOCALE));
    }

}