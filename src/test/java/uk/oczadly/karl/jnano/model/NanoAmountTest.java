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
    
    NanoAmount a = new NanoAmount("1230000000000000000000000000000");
    NanoAmount b = new NanoAmount("1230000000000000000000000000001");
    NanoAmount c = new NanoAmount("1230000000000000000000000000000");
    
    
    @Test
    public void testGetAsRaw() {
        assertEquals(new BigInteger("1230000000000000000000000000000"), a.getAsRaw());
        assertEquals(new BigInteger("1230000000000000000000000000001"), b.getAsRaw());
    }
    
    @Test
    public void testGetAsNano() {
        assertEquals(0, new BigDecimal("1.23").compareTo(a.getAsNano()));
        assertEquals(0, new BigDecimal("1.230000000000000000000000000001").compareTo(b.getAsNano()));
    }
    
    @Test
    public void testGetAs() {
        assertEquals(0, new BigDecimal("1230").compareTo(a.getAs(NanoUnit.KILO)));
    }
    
    @Test
    public void testCompareTo() {
        assertEquals(0, a.compareTo(c));
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }
    
    @Test
    public void testJson() {
        Gson gson = new Gson();
    
        NanoAmount from = gson.fromJson("\"1230000000000000000000000000000\"", NanoAmount.class);
        assertEquals(from, a);
    
        assertEquals("\"1230000000000000000000000000000\"", gson.toJson(a));
    }
    
}