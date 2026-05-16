package uk.oczadly.karl.jnano.model.currency;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static org.junit.Assert.*;

public class ExponentDenominationTest {

    static final Denomination UNIT_E10 = new ExponentDenomination(10, "Coin", "$");
    static final Denomination UNIT_E10_NO_SYMBOL = new ExponentDenomination(10, "Coin", null);
    static final BigInteger VALUE_E10 = new BigInteger("10000000000");
    static final Locale LOCALE = Locale.UK;


    @Test
    public void testConvertFromRaw() {
        assertEquals(BigDecimal.ZERO, UNIT_E10.convertFromRaw(BigInteger.ZERO));
        assertEquals(new BigDecimal("0.0000000001"), UNIT_E10.convertFromRaw(BigInteger.ONE));
        assertEquals(BigDecimal.ONE, UNIT_E10.convertFromRaw(VALUE_E10));
    }

    @Test
    public void testConvertToRaw() {
        assertEquals(BigInteger.ZERO, UNIT_E10.convertToRaw(BigDecimal.ZERO));
        assertEquals(VALUE_E10, UNIT_E10.convertToRaw(BigDecimal.ONE));
        assertEquals(VALUE_E10, UNIT_E10.convertToRaw(new BigDecimal("1.0000000000")));
    }

    @Test
    public void testConvertToRawPrecisionTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> UNIT_E10.convertToRaw(new BigDecimal("1.00000000001")));
        assertThrows(IllegalArgumentException.class, () -> UNIT_E10.convertToRaw(new BigDecimal("1.99999999999")));
    }

    @Test
    public void testNameFormat() {
        assertEquals("0 Coin", UNIT_E10_NO_SYMBOL.format(BigDecimal.ZERO, LOCALE));
        assertEquals("1 Coin", UNIT_E10_NO_SYMBOL.format(BigDecimal.ONE, LOCALE));
        assertEquals("12,345 Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("12345.000"), LOCALE));
        assertEquals("12,345.678 Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("12345.678"), LOCALE));
        assertEquals("1.2 Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("1.2"), LOCALE));
        assertEquals("1.234567 Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("1.234567"), LOCALE));
        assertEquals("1.234567… Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("1.23456799999"), LOCALE));
        assertEquals("0.000000… Coin", UNIT_E10_NO_SYMBOL.format(new BigDecimal("0.0000001"), LOCALE));
    }

    @Test
    public void testSymbolFormat() {
        assertEquals("$0", UNIT_E10.format(BigDecimal.ZERO, LOCALE));
        assertEquals("$1", UNIT_E10.format(BigDecimal.ONE, LOCALE));
        assertEquals("$12,345", UNIT_E10.format(new BigDecimal("12345.000"), LOCALE));
        assertEquals("$12,345.678", UNIT_E10.format(new BigDecimal("12345.678"), LOCALE));
        assertEquals("$1.2", UNIT_E10.format(new BigDecimal("1.2"), LOCALE));
        assertEquals("$1.234567", UNIT_E10.format(new BigDecimal("1.234567"), LOCALE));
        assertEquals("$1.234567…", UNIT_E10.format(new BigDecimal("1.23456799999"), LOCALE));
        assertEquals("$0.000000…", UNIT_E10.format(new BigDecimal("0.0000001"), LOCALE));
    }

}