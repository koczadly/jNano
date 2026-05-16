/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.Gson;
import org.junit.Test;
import uk.oczadly.karl.jnano.model.currency.NanoAmount;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;
import static uk.oczadly.karl.jnano.model.currency.Denomination.NANO;

/**
 * @author Karl Oczadly
 */
public class NanoAmountTest {
    
    NanoAmount VAL_A = NanoAmount.valueOfRaw("1230000000000000000000000000000");
    NanoAmount VAL_B = NanoAmount.valueOfRaw("1230000000000000000000000000001");

    
    @Test(expected = IllegalArgumentException.class)
    public void testOverflowValueOf() {
        NanoAmount.valueOfNano(new BigDecimal("1.0000000000000000000000000000001"));
    }
    
    @Test
    public void testValueOf() {
        assertEquals(new BigInteger("27"), NanoAmount.valueOfRaw("27").getAsRaw());
        assertEquals(new BigInteger("27"), NanoAmount.valueOfRaw(new BigInteger("27")).getAsRaw());
        assertEquals(new BigInteger("27000000000000000000000000000000"),
                NanoAmount.valueOf(27, NANO).getAsRaw());
        assertEquals(new BigInteger("27000000000000000000000000000000"),
                NanoAmount.valueOf(new BigInteger("27"), NANO).getAsRaw());
        assertEquals(new BigInteger("27100000000000000000000000000000"),
                NanoAmount.valueOf(new BigDecimal("27.1"), NANO).getAsRaw());
        assertEquals(new BigInteger("100000"), NanoAmount.valueOfRawExponent(5).getAsRaw());
    }
    
    @Test
    public void testGetAsRaw() {
        assertEquals(NanoAmount.valueOfRaw("1230000000000000000000000000000").getAsRaw(), VAL_A.getAsRaw());
        assertEquals(NanoAmount.valueOfRaw("1230000000000000000000000000001").getAsRaw(), VAL_B.getAsRaw());
    }
    
    @Test
    public void testGetAsNano() {
        assertEquals(0, new BigDecimal("1.23").compareTo(VAL_A.getAsNano()));
        assertEquals(0, new BigDecimal("1.230000000000000000000000000001").compareTo(VAL_B.getAsNano()));
    }

    @Test
    public void testGetAs() {
        assertEquals(0, new BigDecimal("1.23").compareTo(VAL_A.getAs(NANO)));
        assertEquals(0, new BigDecimal("1.230000000000000000000000000001").compareTo(VAL_B.getAs(NANO)));
    }
    
    @Test
    public void testCompareTo() {
        assertEquals(0, VAL_A.compareTo(NanoAmount.valueOfRaw(VAL_A.getAsRaw())));
        assertTrue(VAL_A.compareTo(VAL_B) < 0);
        assertTrue(VAL_B.compareTo(VAL_A) > 0);
    }

    @Test
    public void testToString() {
        assertEquals("Ӿ0", NanoAmount.valueOfRaw(0).toString());
        assertEquals("Ӿ1,234.56", NanoAmount.valueOfNano("1234.5600").toString());
        assertEquals("Ӿ1.123456…", NanoAmount.valueOfNano("1.123456789").toString());
        assertEquals("Ӿ0.000001", NanoAmount.valueOfNano("0.000001").toString());
        assertEquals("900000000000000000000000 raw", NanoAmount.valueOfNano("0.0000009").toString());
        assertEquals("1 raw", NanoAmount.valueOfRaw(1).toString());
    }
    
    @Test
    public void testJson() {
        Gson gson = new Gson();
    
        NanoAmount from = gson.fromJson("\"1230000000000000000000000000000\"", NanoAmount.class);
        assertEquals(from, VAL_A);
    
        assertEquals("\"1230000000000000000000000000000\"", gson.toJson(VAL_A));
    }
    
    @Test
    public void testAdd() {
        assertEquals(NanoAmount.valueOfRaw("567"), NanoAmount.valueOfRaw("456").add(NanoAmount.valueOfRaw("111")));
        assertThrows(ArithmeticException.class, () -> NanoAmount.MAX_VALUE.add(NanoAmount.ONE_RAW));
    }
    
    @Test
    public void testSubtract() {
        assertEquals(NanoAmount.valueOfRaw("345"), NanoAmount.valueOfRaw("456").subtract(NanoAmount.valueOfRaw("111")));
        assertThrows(ArithmeticException.class, () -> NanoAmount.ZERO.subtract(NanoAmount.ONE_RAW));
    }
    
    @Test
    public void testDifference() {
        assertEquals(NanoAmount.valueOfRaw("400"),
                NanoAmount.valueOfRaw("500").difference(NanoAmount.valueOfRaw("100")));
        assertEquals(NanoAmount.valueOfRaw("400"),
                NanoAmount.valueOfRaw("100").difference(NanoAmount.valueOfRaw("500")));
    }
    
}