/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.model;

import com.google.gson.Gson;
import org.junit.Test;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Karl Oczadly
 */
public class NanoAmountTest {
    
    NanoAmount VAL_A = NanoAmount.valueOf("1230000000000000000000000000000");
    NanoAmount VAL_B = NanoAmount.valueOf("1230000000000000000000000000001");
    NanoAmount VAL_C = NanoAmount.valueOf("1230000000000000000000000000000");
    
    
    @Test
    public void testValueOf() {
        assertEquals(new BigInteger("27"), NanoAmount.valueOf("27").getAsRaw());
        assertEquals(new BigInteger("27"), NanoAmount.valueOf(new BigInteger("27")).getAsRaw());
        assertEquals(new BigInteger("27000000000000000000000000000000"),
                NanoAmount.valueOf(27, NanoUnit.MEGA).getAsRaw());
        assertEquals(new BigInteger("27000000000000000000000000000000"),
                NanoAmount.valueOf(new BigInteger("27"), NanoUnit.MEGA).getAsRaw());
        assertEquals(new BigInteger("27100000000000000000000000000000"),
                NanoAmount.valueOf(new BigDecimal("27.1"), NanoUnit.MEGA).getAsRaw());
    }
    
    @Test
    public void testGetAsRaw() {
        assertEquals(NanoAmount.valueOf("1230000000000000000000000000000").getAsRaw(), VAL_A.getAsRaw());
        assertEquals(NanoAmount.valueOf("1230000000000000000000000000001").getAsRaw(), VAL_B.getAsRaw());
    }
    
    @Test
    public void testGetAsNano() {
        assertEquals(0, new BigDecimal("1.23").compareTo(VAL_A.getAsNano()));
        assertEquals(0, new BigDecimal("1.230000000000000000000000000001").compareTo(VAL_B.getAsNano()));
    }
    
    @Test
    public void testGetAs() {
        assertEquals(0, new BigDecimal("1230").compareTo(VAL_A.getAs(NanoUnit.KILO)));
    }
    
    @Test
    public void testCompareTo() {
        assertEquals(0, VAL_A.compareTo(VAL_C));
        assertTrue(VAL_A.compareTo(VAL_B) < 0);
        assertTrue(VAL_B.compareTo(VAL_A) > 0);
    }
    
    @Test
    public void testJson() {
        Gson gson = new Gson();
    
        NanoAmount from = gson.fromJson("\"1230000000000000000000000000000\"", NanoAmount.class);
        assertEquals(from, VAL_A);
    
        assertEquals("\"1230000000000000000000000000000\"", gson.toJson(VAL_A));
    }
    
    @Test
    public void testArithmetic() {
        assertEquals(NanoAmount.valueOf("567"), NanoAmount.valueOf("456").add(NanoAmount.valueOf("111")));
        assertEquals(NanoAmount.valueOf("345"), NanoAmount.valueOf("456").subtract(NanoAmount.valueOf("111")));
        assertEquals(NanoAmount.valueOf("912"), NanoAmount.valueOf("456").multiply(NanoAmount.valueOf("2")));
        assertEquals(NanoAmount.valueOf("912"), NanoAmount.valueOf("456").multiply(2));
        assertEquals(NanoAmount.valueOf("228"), NanoAmount.valueOf("456").divide(NanoAmount.valueOf("2")));
        assertEquals(NanoAmount.valueOf("228"), NanoAmount.valueOf("456").divide(2));
    }
    
}