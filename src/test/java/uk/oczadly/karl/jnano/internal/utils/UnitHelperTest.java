/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import org.junit.Test;
import uk.oczadly.karl.jnano.model.NanoAmount;
import uk.oczadly.karl.jnano.util.NanoUnit;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class UnitHelperTest {

    @Test
    public void testFormat() {
        // Symbol prefix
        assertEquals("Ӿ1", format("1000000000000000000000000000000", NanoUnit.MEGA, false));

        // Display name
        assertEquals("1 raw", format("1", NanoUnit.RAW, false));

        // Numerical formatting
        assertEquals("Ӿ0", format("0", NanoUnit.MEGA, false));
        assertEquals("Ӿ1", format("1000000000000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ1.01", format("1010000000000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ1,234", format("1234000000000000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ1,234.01", format("1234010000000000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ0.000001", format("1000000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ0.000000…", format("100000000000000000000000", NanoUnit.MEGA, false));
        assertEquals("Ӿ0.000000…", format("1", NanoUnit.MEGA, false));

        // Numerical formatting with useRaw
        assertEquals("Ӿ1", format("1000000000000000000000000000000", NanoUnit.MEGA, true));
        assertEquals("Ӿ1.01", format("1010000000000000000000000000000", NanoUnit.MEGA, true));
        assertEquals("Ӿ0.000001", format("1000000000000000000000000", NanoUnit.MEGA, true));
        assertEquals("100,000,000,000,000,000,000,000 raw", format("100000000000000000000000", NanoUnit.MEGA, true));
        assertEquals("1 raw", format("1", NanoUnit.MEGA, true));
    }

    private static String format(String raw, NanoAmount.Denomination unit, boolean useRaw) {
        return UnitHelper.format(new BigInteger(raw), unit, useRaw);
    }

}